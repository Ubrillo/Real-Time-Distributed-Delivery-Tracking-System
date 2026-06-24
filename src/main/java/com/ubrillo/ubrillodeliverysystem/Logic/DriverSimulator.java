package com.ubrillo.ubrillodeliverysystem.Logic;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DriverSimulator {

    private final SimpMessagingTemplate messagingTemplate;

    private double lat = 51.5074;  // London start
    private double lng = -0.1278;

    public DriverSimulator(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 2000) // every 2 seconds
    public void simulateMovement() {

        // simulate movement (random walk)
        lat += (Math.random() - 0.5) * 0.001;
        lng += (Math.random() - 0.5) * 0.001;

        DriverLocationRequest update = new DriverLocationRequest();
        update.setDriverId("SIM_DRIVER_1");
        update.setLatitude(lat);
        update.setLongitude(lng);

        System.out.println("Simulated driver update: " + update);

        // push to all users
        messagingTemplate.convertAndSend("/gps/topic/user/", update);
    }
}