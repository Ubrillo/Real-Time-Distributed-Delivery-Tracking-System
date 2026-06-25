package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * Service responsible for managing delivery drivers,
 * including creation, retrieval, and zone assignment.
 */
@Service
public class DriverManagement {

    @Getter
    HashMap<String, DeliveryDriver> drivers;

    /**
     * Initializes driver storage and loads default drivers.
     */
    public DriverManagement (){
        drivers = new HashMap<>();
        loadDrivers();
    }

    /**
     * Retrieves a driver by name.
     */
    public DeliveryDriver getDriver(String name){
        return drivers.get(name);
    }

    /**
     * Loads default drivers into the system.
     */
    private void loadDrivers(){
        String names[] = {"Jack", "Dave", "Joe", "Mike"};

        double lat = GpsService.getBaseCoordinate().latitude();
        double lon = GpsService.getBaseCoordinate().longitude();
        Zone zone = null;

        for (int i=0; i < names.length; i++){
            drivers.put(
                    names[i],
                    new DeliveryDriver(names[i], Integer.toString(i+1))
            );
        }
    }

    /**
     * Assigns a delivery zone to a specific driver.
     */
    public void assignDeliveryZone(String name, Zone zone){
        DeliveryDriver driver = drivers.get(name);
        driver.setDeliveryZone(zone);
    }
}
