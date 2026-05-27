package com.ubrillo.ubrillodeliverysystem.Controller;
import com.ubrillo.ubrillodeliverysystem.Logic.*;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderState;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderStateStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class Controller   {

    @Autowired
    private OrderManager orderManager;


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
        //
    }

    @GetMapping("api/view-order")
    public OrderState getOrder(@RequestBody Request request){
        return orderManager.getOrder(request);
    }

    /*===================DRIVER APIS =======================*/
    @PutMapping("api/driver-delivery-out-scan")
    public void deliveryOutScan(@RequestBody DeliveryScanRequest request) {

        orderManager.deliveryOutScan(request);
    }

    public void deliveredOutScan(@RequestBody Request request) {
        orderManager.deliveredOutScan(request);
    }
}
