package com.ubrillo.ubrillodeliverysystem;

import com.ubrillo.ubrillodeliverysystem.Controller.Controller;
import com.ubrillo.ubrillodeliverysystem.Logic.Coordinate;
import com.ubrillo.ubrillodeliverysystem.Logic.GeocodingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class UbrilloDeliverySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(UbrilloDeliverySystemApplication.class, args);
        //Controller management = new Controller();

        GeocodingService geo = new GeocodingService();
        Coordinate result = geo.getCoordinates("E32AF");
        System.out.println(result);

    }
}