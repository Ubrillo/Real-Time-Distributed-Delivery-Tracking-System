

var trackingPage = document.querySelector('#tracking-page');
var mapPage =  document.querySelector('#map');
var trackingForm = document.querySelector('#trackingForm');



let map;
let driverMarker;
let pickupMarker;
let destinationMarker;

let stompClient = null;
let orderId = null;

const backendBaseUrl = "http://localhost:8080";


// =========================
// Get Order ID safely
// =========================
function getOrderId() {
    const id = document.querySelector('#orderId').value.trim();
    console.log("Order ID:", id);
    return id;
}


// =========================
// Fetch tracking data (REST)
// =========================
async function fetchInitialTrackingData() {

    if (!orderId) {
        throw new Error("Order ID is missing");
    }

    const response = await fetch(
        `${backendBaseUrl}/api/track-order/${orderId}`
    );

    if (!response.ok) {
        throw new Error("Failed to fetch tracking data");
    }

    return response.json();
}


// =========================
// Initialize Google Map
// =========================
async function initMap() {

    const tracking = await fetchInitialTrackingData();
    console.log(tracking);
    const { Map } = await google.maps.importLibrary("maps");
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");

    map = new Map(document.getElementById("map"), {
        center: tracking.location,
        zoom: 14,
        mapId: "DEMO_MAP_ID"
    });

    pickupMarker = new AdvancedMarkerElement({
        map,
        position: tracking.location,
        title: "Pickup location"
    });

    destinationMarker = new AdvancedMarkerElement({
        map,
        position: tracking.destination,
        title: "Destination location"
    });

    driverMarker = new AdvancedMarkerElement({
        map,
        position: tracking.location,
        title: "Driver location"
    });

    updateStatus(tracking.status);

    connectWebSocket();
}


// =========================
// WebSocket connection (STOMP)
// =========================
function connectWebSocket() {

    const socket = new SockJS(`${backendBaseUrl}/ws/user`);

    stompClient = new StompJs.Client({
        webSocketFactory: () => socket,

        onConnect: () => {

            console.log("WebSocket connected");

            stompClient.subscribe(
                `/gps/topic/user/${orderId}`,
                (message) => {
                    const update = JSON.parse(message.body);
                    handleTrackingUpdate(update);
                }
            );

            // Send tracking request
            stompClient.publish({
                destination: "/app/track-order",
                body: JSON.stringify({ requestId: orderId })
            });
        },

        onStompError: (frame) => {
            console.error("WebSocket error:", frame);
            updateStatus("Connection error");
        }
    });

    stompClient.activate();
}


// =========================
// Handle live updates
// =========================
function handleTrackingUpdate(update) {

    if (update.status) {
        updateStatus(update.status);
    }

    if (update.location && driverMarker) {

        driverMarker.position = update.location;

        map.panTo(update.location);
    }
}


// =========================
// Update UI status
// =========================
function updateStatus(status) {
    document.getElementById("status").textContent = status;
}


// =========================
// Submit handler (MAIN ENTRY)
// =========================
async function handleSubmit(event) {

    event.preventDefault();

    orderId = getOrderId();

    if (!orderId) {
        alert("Please enter order ID");
        return;
    }

    try {
        trackingPage.classList.add('hidden');
        mapPage.classList.remove('hidden');

        await initMap();

    } catch (error) {
        console.error(error);
        updateStatus("Failed to load tracking data");
    }
}


// =========================
// Event listener (ONLY ENTRY POINT)
// =========================
trackingForm.addEventListener('submit', handleSubmit);