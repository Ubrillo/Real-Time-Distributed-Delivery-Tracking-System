package com.ubrillo.ubrillodeliverysystem.Controller;
import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import com.ubrillo.ubrillodeliverysystem.Logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class Controller {

    @Autowired
    private OrderManager orderManager;

    /*===========================USER API===================*/
    @PostMapping("api/create-request")
    public newRequestResponse requestReceiver(@RequestBody Request request) {
        newRequestResponse response = orderManager.createOrder(request);
        return response;
    }

    @PostMapping("api/cancel-request")
    public void cancelOrder(@RequestBody Request request) {
        orderManager.cancelOrder(request);
    }

    @GetMapping("api/track-order")
    public void trackOrder(Request order) {
        orderManager.trackOrder(order);
        System.out.println("calling...");
    }

    @GetMapping("api/view-order")
    public Request getOrder(@RequestBody Request request) {
        return orderManager.getOrder(request);
    }

    @GetMapping("api/view-order-db")
    public Request getOrderFromDB(@RequestBody Request request) {
        return orderManager.getOrderFromDb(request);
    }

    @GetMapping("api/track-order/{orderId}")
    @SendTo("/gps/topic/user")
    public synchronized OrderState trackOrder(@PathVariable String orderId) {
        Request request = new Request("", orderId, "", null, null, "", "", "", "");
        System.out.println("calling...:" + orderId);
        return orderManager.trackOrder(request);
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
    public synchronized void updateGps(@RequestBody signalGPS signal) {
        orderManager.updateOrderGps(signal);
        //System.out.println(signal.toString());
    }

    /*=====================ADMIN APIS===================*/
    @GetMapping("api/admin/view-order")
    public OrderState getOrderDetails(@RequestBody Request request) {
        return orderManager.getOrderDetails(request);
    }


}