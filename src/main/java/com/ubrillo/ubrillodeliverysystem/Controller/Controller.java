package com.ubrillo.ubrillodeliverysystem.Controller;

import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class Controller   {
    @Autowired
    RequestService requestMan;

    @Autowired
    OrderList orderList;

    @Autowired
    DispatchQueue2nd dispatchQueue2nd;

    @Autowired
    BatchDispatcher batchDispatcher;

    @Autowired
    private DatabaseAPI databaseAPI;


    @PostMapping("api/create-request")
    public newRequestResponse requestReceiver(@RequestBody Request request){
        Request order = requestMan.createRequest(request);

        databaseAPI.insertOrder(order);

        orderList.addOrder(order);
        batchDispatcher.checkSizeTrigger();
        System.out.println(order.getRequestId() + " is created. orderNo: "+ orderList.getSize());
        return new newRequestResponse(order);
    }

    @PostMapping("api/cancel-request")
    public void cancelOrder(@RequestBody Request request){
        String id = request.getRequestId();
        orderList.removeOrderById(id);
        System.out.println(request.getRequestId() + " is cancelled: "+ orderList.getSize());
    }

    @GetMapping("api/track-order/{orderId}")
    public void trackOrder(String orderId){

    }



    @GetMapping("api/view-order")
    public List<Request> getOrders(){
        return databaseAPI.getOrders();
    }

    /*===================DRIVER APIS =======================*/

    @PutMapping("/api/driver-delivery-out-scan")
    public void deliveryOutScan(@RequestBody DeliveryScanRequest req) {
            Request request = dispatchQueue2nd.getNextOrder(req.getDeliveryZone());
            request.setStatus(RequestStatus.OUTOFDELIVERY);
            System.out.println(request.getRequestId() + " is out of delivery!!!");
    }

    @PutMapping("api/v1/driver-devlivered-out-scan")
    public void deliveredOutScan(@PathVariable  String orderId){
        //fetch data using database api to update order status to delivered.
    }
}
