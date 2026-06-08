package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class DriverManagement {
    DriverManagement (){}

    @Getter
    HashMap<String, DeliveryDriver> drivers;

    public DeliveryDriver getDriver(String name){
        return drivers.get(name);
    }

     private void loadDrivers(){
        String names[] = {"Jack", "Dave"};

        for (int i=0; i < names.length; i++){
            drivers.put(
                    names[i],
                    new DeliveryDriver(
                            names[i],
                            Integer.toString(i+1),
                            new Location(0., 0.),
                            new Coordinate(0., 0.)
                    )
            );
        }
    }
}
