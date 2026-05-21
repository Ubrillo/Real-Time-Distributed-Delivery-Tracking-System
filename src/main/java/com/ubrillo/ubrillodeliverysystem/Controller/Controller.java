package com.ubrillo.ubrillodeliverysystem.Controller;
import com.ubrillo.ubrillodeliverysystem.Logic.*;
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
    public  void getOrder(@RequestBody Request request){
        orderManager.getOrder(request);
    }

    /*===================DRIVER APIS =======================*/
    @PutMapping()
    public void deliveryOutScan(@RequestBody DeliveryScanRequest request) {
            orderManager.deliveryOutScan(request);
    }

    public void deliveredOutScan(@PathVariable  String orderId){
        //fetch data using database api to update order status to delivered.
    }
}

