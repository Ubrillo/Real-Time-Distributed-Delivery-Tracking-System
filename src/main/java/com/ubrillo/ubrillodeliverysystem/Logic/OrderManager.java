package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Events.Message;
import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class OrderManager {
    @Autowired
    RequestService requestMan;

    @Autowired
    OrderList orderList;

    @Autowired
    DispatchQueue2nd dispatchQueue2nd;

//    @Autowired
//    BatchDispatcher batchDispatcher;

    @Autowired
    private DatabaseAPI databaseAPI;

    @Autowired
    private OrderEventProducer orderEventProducer;

    private static final int BATCH_SIZE = 5;

    public void OrderManager(){}

    public newRequestResponse createOrder(Request request){
        Request order = requestMan.createRequest(request);
        databaseAPI.insertOrder(order);
        Notification event = messageParser(order);
        orderEventProducer.publishOrderCreated(event);
        orderList.addOrder(order);
        checkSizeTrigger();
        return new newRequestResponse(order);
    }

    public void cancelOrder(Request request){
        String id = request.getRequestId();
        Request order  = orderList.getOrder(id);
        order.setStatus(RequestStatus.CANCELLED);
        orderList.removeOrderById(id);
        databaseAPI.updateOrderStatus(request.getRequestId(), order.getStatus());
        //messageParser(or)
        System.out.println(request.getRequestId() + " is cancelled: "+ orderList.getSize());

        Notification event = messageParser(order);
        orderEventProducer.publishOrderCreated(event);
    }

    public void trackOrder(Request order){
        //
    }

    public  newRequestResponse getOrder(@RequestBody Request request){
        Request order = databaseAPI.getOrder(request.getRequestId());
        return new newRequestResponse(order);
    }

    /*===================DRIVER APIS =======================*/

    public void deliveryOutScan(@RequestBody DeliveryScanRequest req) {
        Request order = dispatchQueue2nd.getNextOrder(req.getDeliveryZone());
        System.out.println(order.getRequestId() + " is out of delivery!!!");
    }

    public void deliveredOutScan(@PathVariable String orderId){
        //fetch data using database api to update order status to delivered.
    }

    // SIZE-BASED + TIME-BASED trigger
    @Scheduled(fixedDelay = 10*60*1000) //every 10mins
    public void timeBasedBatch(){
        processBatch();
    }

    public void checkSizeTrigger(){
        if (orderList.getSize() >= BATCH_SIZE){
            processBatch();
        }
    }

    private synchronized void processBatch(){
        List<Request> batch = orderList.getBatch(BATCH_SIZE);
        if (batch.isEmpty()) return;

        for (Request order: batch){
            dispatchQueue2nd.addOrder(order);
            order.setStatus(RequestStatus.DISPATCHED);

            databaseAPI.updateOrderStatus(
                    order.getRequestId(),
                    order.getStatus()
            );

            Notification event = messageParser(order);
            orderEventProducer.publishOrderCreated(event);
        }
//        System.out.println("Batch moved: " + batch.size());
    }

    private Notification messageParser(Request order){
        RequestStatus eventType = order.getStatus();
        String sender = "ubrillo-delivery@org.uk";
        String recipient = order.getCustomerName()+"@mail.com";
        String description = order.getDescription();
        String time = requestMan.getCurrentTime();
        String properties = order.getRequestId();

        String message = "";
        switch (eventType){
            case CREATED -> message = "📦Your order has been received";
            case DISPATCHED -> message = "📦Your order has been dispatched";
            case CANCELLED -> message = "📦Your order has been cancelled";
            case DELIVERED -> message = "📦Your order has been delivered";
            case OUTFORDELIVERY -> message = "📦Your order is out for delivery!!!";
        }
        return new Notification(
                sender,
                recipient,
                description,
                time,
                properties,
                message
        );
    }
}
