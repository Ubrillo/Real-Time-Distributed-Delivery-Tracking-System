'use strict'


var trackingPage = document.querySelector('#tracking-page');
var mapPage =  document.querySelector('#map');
var trackingForm = document.querySelector('#trackingForm');

var stompClient = null;
var orderId = null;

let map;
let driverMarker;
let pickupMarker;
let destinationMarker;


function connect(event){

    orderId = document.querySelector('#orderId').value.trim();

    if(orderId){
        trackingPage.classList.add('hidden');
        mapPage.classList.remove('hidden');

        var socket = new SocketJS('/ws')

        stompClient = stomp.over(socket);

        stompClient.connect({}, onConnected, onError)
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/gps/topic', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/gps/track-order",
        {},
        JSON.stringify({requestId: orderId})
    )

    connectingElement.classList.add('hidden');
}

function onError(error){
        connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        connectingElement.style.color = 'red';
}

function onMessageReceived(payload){

}

async function initMap() {
  try {
    const tracking = await fetchInitialTrackingData(orderId);

    const { Map } = await google.maps.importLibrary("maps");
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");

    map = new Map(document.getElementById("map"), {
      center: tracking.driverLocation || tracking.pickup,
      zoom: 14,
      mapId: "DEMO_MAP_ID"
    });

    pickupMarker = new AdvancedMarkerElement({
      map,
      position: tracking.pickup,
      title: "Pickup location"
    });

    destinationMarker = new AdvancedMarkerElement({
      map,
      position: tracking.destination,
      title: "Destination location"
    });

    driverMarker = new AdvancedMarkerElement({
      map,
      position: tracking.driverLocation || tracking.pickup,
      title: "Driver location"
    });

    updateStatus(tracking.status);
    connectWebSocket(orderId);
  } catch (error) {
    console.error("Failed to initialize tracking map:", error);
    updateStatus("Unable to load tracking data");
  }
}

async function fetchInitialTrackingData(orderId) {
  const response = await fetch("${backendBaseUrl}/api/track-order");

  if (!response.ok) {
    throw new Error("Failed to fetch tracking data");
  }

  return response.json();
}
