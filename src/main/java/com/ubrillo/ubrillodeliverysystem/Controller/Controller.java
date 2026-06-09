package com.ubrillo.ubrillodeliverysystem.Controller;
import com.ubrillo.ubrillodeliverysystem.Logic.*;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class Controller   {

    @Autowired
    private OrderManager orderManager;

//    List<signalGPS> route = List.of(
//            new signalGPS(new Coordinate(51.5074, -0.1278)),
//            new signalGPS(new Coordinate(51.5076, -0.1275)),
//            new signalGPS(new Coordinate(51.5079, -0.1271)),
//            new signalGPS(new Coordinate(51.5082, -0.1267))
//    );


    @PostMapping("api/create-request")
    public void requestReceiver(@RequestBody Request request){
        orderManager.createOrder(request);
    }

    @PostMapping("api/cancel-request")
    public void cancelOrder(@RequestBody Request request){
        orderManager.cancelOrder(request);
    }

    @GetMapping("api/track-order")
    public void trackOrder(Request order){
        orderManager.trackOrder(order);
        System.out.println("calling...");
    }

    @GetMapping("api/view-order")
    public OrderState getOrder(@RequestBody Request request){
        return orderManager.getOrder(request);
    }

    @GetMapping("api/view-order-db")
    public Request getOrderFromDB(@RequestBody Request request){
        return orderManager.getOrderFromDb(request);
    }

    /*===================DRIVER APIS =======================*/
    @PutMapping("api/driver-delivery-out-scan")
    public void deliveryOutScan(@RequestBody DeliveryScanRequest request) {
        orderManager.deliveryOutScan(request);
    }

    @PutMapping("api/order-delivered-out-scan")
    public void deliveredOutScan(@RequestBody Request request) {
        orderManager.deliveredOutScan(request);
    }

    @PutMapping("api/gps.signal")
    public synchronized void updateGps(@RequestBody signalGPS signal){
        orderManager.updateOrderGps(signal);
        //System.out.println(signal.toString());
    }

    @GetMapping("api/track-order/{orderId}")
    @SendTo("/gps/topic/user")
    public synchronized OrderState trackOrder(@PathVariable String orderId){
        Request request = new Request("", orderId, "", null, null, "","","");
        System.out.println("calling...:"+orderId);
        return orderManager.trackOrder(request);
    }

//    int index = 0;
//    @Scheduled(fixedRate = 1000)
//    public void simulateDriver() {
//        signalGPS signal = route.get(index);
//        updateGps(signal);
//        index = (index + 1) % route.size();
//    }
}
