package com.ubrillo.ubrillodeliverysystem.Logic;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DriverSimulator {

    private final SimpMessagingTemplate messagingTemplate;

    private final Map<String, DeliveryDriver> activeDrivers = new ConcurrentHashMap<>();

    public DriverSimulator(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void addDriver(DeliveryDriver driver) {
        activeDrivers.put(driver.getId(), driver);
    }

    public void removeDriver(String driverId) {
        activeDrivers.remove(driverId);
    }

    @Scheduled(fixedRate = 2000)
    public void simulateAllDrivers() {
        for (DeliveryDriver driver : activeDrivers.values()) {
            moveDriverRandomly(driver);
            sendDriverLocation(driver);
        }
    }

    private void moveDriverRandomly(DeliveryDriver driver) {
        Location current = driver.getCurrentLocation();

        double newLat = current.getLatitude() + (Math.random() - 0.5) * 0.001;
        double newLng = current.getLongitude() + (Math.random() - 0.5) * 0.001;

        driver.moveTo(newLat, newLng);
    }

    private void sendDriverLocation(DeliveryDriver driver) {
        DriverLocationRequest update = new DriverLocationRequest();
        update.setDriverId(driver.getId());
        update.setLatitude(driver.getCurrentLocation().getLatitude());
        update.setLongitude(driver.getCurrentLocation().getLongitude());

        messagingTemplate.convertAndSend("/gps/location/" + driver.getId(), update);
        messagingTemplate.convertAndSend("/gps/location", update);
    }
}