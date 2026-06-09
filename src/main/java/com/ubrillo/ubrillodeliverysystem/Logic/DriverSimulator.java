//package com.ubrillo.ubrillodeliverysystem.Logic;
//
//import com.ubrillo.ubrillodeliverysystem.Controller.Controller;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.util.List;
//
//public class DriverSimulator {
//    List<signalGPS> route = List.of(
//            new signalGPS(new Coordinate(51.5074, -0.1278)),
//            new signalGPS(new Coordinate(51.5076, -0.1275)),
//            new signalGPS(new Coordinate(51.5079, -0.1271)),
//            new signalGPS(new Coordinate(51.5082, -0.1267))
//    );
//
//    @Autowired
//    Controller controller;
//
//    int index = 0;
//
//    @Scheduled(fixedRate = 1000)
//    public void simulateDriver() {
//
//        signalGPS signal = route.get(index);
//        controller.updateGps(signal);
//        index = (index + 1) % route.size();
//    }
//
//    public DriverSimulator(){}
//
//}
