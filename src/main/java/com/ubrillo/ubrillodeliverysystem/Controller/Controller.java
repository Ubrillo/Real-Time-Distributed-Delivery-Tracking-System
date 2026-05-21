package com.ubrillo.ubrillodeliverysystem.Controller;
import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
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

    @Autowired
    private  OrderEventProducer orderEventProducer;

    @PostMapping("api/create-request")
    public newRequestResponse requestReceiver(@RequestBody Request request){
        Request order = requestMan.createRequest(request);
        databaseAPI.insertOrder(order);

        OrderEvent event = new OrderEvent(order);

        orderEventProducer.publishOrderCreated(event);

        orderList.addOrder(order);
        batchDispatcher.checkSizeTrigger();
        System.out.println(order.getRequestId() + " is created. orderNo: "+ orderList.getSize());

        return new newRequestResponse(order);
    }

    @PostMapping("api/cancel-request")
    public void cancelOrder(@RequestBody Request request){
        String id = request.getRequestId();
        orderList.removeOrderById(id);
        databaseAPI.updateOrderStatus(request.getRequestId(), RequestStatus.CANCELLED);
        System.out.println(request.getRequestId() + " is cancelled: "+ orderList.getSize());
    }

    @GetMapping("api/track-order")
    public void trackOrder(Request order){

    }

    @GetMapping("api/view-order")
    public  newRequestResponse getOrder(@RequestBody Request request){
        Request order = databaseAPI.getOrder(request.getRequestId());
        return new newRequestResponse(order);
    }

    /*===================DRIVER APIS =======================*/
    @PutMapping("/api/driver-delivery-out-scan")
    public void deliveryOutScan(@RequestBody DeliveryScanRequest req) {
            Request order = dispatchQueue2nd.getNextOrder(req.getDeliveryZone());
            System.out.println(order.getRequestId() + " is out of delivery!!!");
    }

    @PutMapping("api/v1/driver-devlivered-out-scan")
    public void deliveredOutScan(@PathVariable  String orderId){
        //fetch data using database api to update order status to delivered.
    }
}

