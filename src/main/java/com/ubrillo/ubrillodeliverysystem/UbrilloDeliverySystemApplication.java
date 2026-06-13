package com.ubrillo.ubrillodeliverysystem;

import com.ubrillo.ubrillodeliverysystem.Controller.Controller;
import com.ubrillo.ubrillodeliverysystem.Logic.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@EnableCaching
@RestController
@SpringBootApplication
public class UbrilloDeliverySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(UbrilloDeliverySystemApplication.class, args);

        Controller controller = new Controller();

//        DeliveryRouteService drs = new DeliveryRouteService();
//
//        GeocodingService gcs = new GeocodingService();
//
//        Coordinate xy = gcs.getCoordinates("WC2N5DU");
//        System.out.println(xy);
//        DeliveryStop stop0 = new DeliveryStop("depot", "WC2N5DU", xy.latitude(), xy.longitude());
//
//        xy = gcs.getCoordinates("E15DG");
//        System.out.println(xy);
//        DeliveryStop stop1 = new DeliveryStop("whitechapel", "E15DG", xy.latitude(), xy.longitude());
//
//        xy = gcs.getCoordinates("E32AF");
//        System.out.println(xy);
//        DeliveryStop stop2 = new DeliveryStop("bowroad", "E32AF", xy.latitude(), xy.longitude());
//
//
//        List<DeliveryStop> stops = List.of(stop0, stop1, stop2);
//
//        RouteResult result = drs.optimizeRoute(stops);
//
//        System.out.println(result.stops());
//        System.out.println(result.totalDriveSeconds());

    }
}