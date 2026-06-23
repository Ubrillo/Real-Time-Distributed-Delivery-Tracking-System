package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class DriverManagement {
    @Getter
    HashMap<String, DeliveryDriver> drivers;

    public DriverManagement (){
        drivers = new HashMap<>();
        loadDrivers();
    }
    
    public DeliveryDriver getDriver(String name){
        return drivers.get(name);
    }

     private void loadDrivers(){
        String names[] = {"Jack", "Dave", "Joe", "Mike"};

        double lat = GpsService.getBaseCoordinate().latitude();
        double lon = GpsService.getBaseCoordinate().longitude();
        Zone zone = null;
        for (int i=0; i < names.length; i++){
//            zone = switch (names[i]) {
//                case "Jack" -> Zone.NORTHWEST;
//                case "Dave" -> Zone.NORTHEAST;
//                case "Joe" -> Zone.SOUTHWEST;
//                case "Mike" -> Zone.SOUTHEAST;
//                default -> zone;
//            };
            drivers.put(
                    names[i],
                    new DeliveryDriver(
                            names[i],
                            Integer.toString(i+1),
                            new Location(lat, lon),
                            //new Location(lat, lon),
                            new Coordinate(lat, lon)
                    )
            );
        }
    }
}
