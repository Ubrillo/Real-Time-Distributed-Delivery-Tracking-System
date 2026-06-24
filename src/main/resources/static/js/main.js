let trackingPage = document.querySelector('#tracking-page');
let mapPage = document.querySelector('#map');
let trackingForm = document.querySelector('#trackingForm');

let map;
let driverMarker;
let pickupMarker;
let destinationMarker;

let directionsService;
let directionsRenderer;

let stompClient = null;
let orderId = null;

const backendBaseUrl = "http://localhost:8080";


// =========================
// Get Order ID
// =========================
function getOrderId() {
    return document.querySelector('#orderId').value.trim();
}


// =========================
// Fetch initial data
// =========================
async function fetchInitialTrackingData() {

    const response = await fetch(
        `${backendBaseUrl}/api/track-order/${orderId}`
    );

    if (!response.ok) {
        throw new Error("Failed to fetch tracking data");
    }

    return response.json();
}


// =========================
// Calculate direction angle
// =========================
function calculateHeading(start, end) {

    const lat1 = start.lat * Math.PI / 180;
    const lng1 = start.lng * Math.PI / 180;
    const lat2 = end.lat * Math.PI / 180;
    const lng2 = end.lng * Math.PI / 180;

    const y = Math.sin(lng2 - lng1) * Math.cos(lat2);
    const x = Math.cos(lat1) * Math.sin(lat2) -
              Math.sin(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1);

    const brng = Math.atan2(y, x);

    return (brng * 180 / Math.PI + 360) % 360;
}


// =========================
// INIT MAP
// =========================
async function initMap() {

    const tracking = await fetchInitialTrackingData();

    const { Map } = await google.maps.importLibrary("maps");

    map = new Map(document.getElementById("map"), {
        center: tracking.currentLocation,
        zoom: 14,
        mapId: "DEMO_MAP_ID"
    });

    const { Marker } = await google.maps.importLibrary("marker");


    // =========================
    // Pickup Marker
    // =========================
    pickupMarker = new Marker({
        map,
        position: tracking.currentLocation,
        title: "Pickup",
        label: "P"
    });


    // =========================
    // Destination Marker
    // =========================
    destinationMarker = new Marker({
        map,
        position: tracking.destination,
        title: "Destination",
        label: "D"
    });


    // =========================
    // DRIVER MARKER (ARROW)
    // =========================
    driverMarker = new google.maps.Marker({
        map: map,
        position: tracking.currentLocation,
        title: "Driver",
        label: {
            text: "Driver",
            color: "#000"
        },
        icon: {
            path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
            scale: 5,
            strokeColor: "#FF0000",
            rotation: 0
        }
    });


    // =========================
    // DIRECTIONS ROUTE
    // =========================
    directionsService = new google.maps.DirectionsService();

    directionsRenderer = new google.maps.DirectionsRenderer({
        map: map,
        suppressMarkers: true,
        polylineOptions: {
            strokeColor: "#0066ff",
            strokeWeight: 5
        }
    });


    drawRoute(tracking.currentLocation, tracking.destination);

    updateStatus(tracking.status);

    connectWebSocket();
}


// =========================
// Draw route
// =========================
function drawRoute(start, end) {

    directionsService.route(
        {
            origin: start,
            destination: end,
            travelMode: google.maps.TravelMode.DRIVING
        },
        (result, status) => {

            if (status === "OK") {
                directionsRenderer.setDirections(result);
            } else {
                console.error("Directions failed:", status);
            }
        }
    );
}


// =========================
// WebSocket
// =========================
function connectWebSocket() {

    const socket = new SockJS(`${backendBaseUrl}/ws/user`);

    stompClient = new StompJs.Client({
        webSocketFactory: () => socket,

        onConnect: () => {

            stompClient.subscribe(
                `/gps/topic/user/${orderId}`,
                (message) => {
                    const update = JSON.parse(message.body);
                    handleTrackingUpdate(update);
                }
            );

            stompClient.publish({
                destination: "/app/track-order",
                body: JSON.stringify({ requestId: orderId })
            });
        },

        onStompError: (frame) => {
            console.error(frame);
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

    if (update.currentLocation && driverMarker) {

        const prev = driverMarker.getPosition();

        const newPos = update.currentLocation;

        // rotate arrow
        const heading = calculateHeading(
            { lat: prev.lat(), lng: prev.lng() },
            newPos
        );

        driverMarker.setIcon({
            path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
            scale: 5,
            strokeColor: "#FF0000",
            rotation: heading
        });

        driverMarker.setPosition(newPos);

        map.panTo(newPos);
    }
}


// =========================
// Status UI
// =========================
function updateStatus(status) {
    document.getElementById("status").textContent = status;
}


// =========================
// Submit
// =========================
async function handleSubmit(event) {

    event.preventDefault();

    orderId = getOrderId();

    if (!orderId) {
        alert("Enter order ID");
        return;
    }

    trackingPage.classList.add("hidden");
    mapPage.classList.remove("hidden");

    await initMap();
}


trackingForm.addEventListener("submit", handleSubmit);