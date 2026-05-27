package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderState;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderStateStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Service
public class OrderManager{
    @Autowired
    RequestService requestMan;

    @Autowired
    OrderList orderList;

    @Autowired
    DispatchQueue2nd dispatchQueue2nd;

    @Autowired
    OrderStateStore orderStateStore;

    @Autowired
    BatchDispatcher batchDispatcher;

    @Autowired
    DatabaseAPI databaseAPI;

    @Autowired
    OrderEventProducer orderEventProducer;


    public void OrderManager(){}

    public newRequestResponse createOrder(Request request){
        Request order = requestMan.createRequest(request);

        databaseAPI.insertOrder(order);

        orderList.addOrder(order);
        batchDispatcher.checkSizeTrigger();

        Notification event = messageParser(order);
        orderEventProducer.publishOrderCreated(event);

        return new newRequestResponse(order);
    }

    public void cancelOrder(Request request){
        String id = request.getRequestId();
        Request order  = orderList.getOrder(id);
        order.setStatus(RequestStatus.CANCELLED);
        orderList.removeOrderById(id);



        //----------------Events--------------
        //sent out notification messages
        Notification event = messageParser(order);
        orderEventProducer.publishOrderCreated(event);

        //updates order states consumers
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

    }

    public OrderState getOrder(Request request){
        return orderStateStore.getState(request.getRequestId());
    }

    public void trackOrder(Request order){
        //
    }
//
//    public  newRequestResponse getOrder(@RequestBody Request request){
//        Request order = databaseAPI.getOrder(request.getRequestId());
//        return new newRequestResponse(order);
//    }


    /*===================DRIVER APIS =======================*/

    public void deliveryOutScan(@RequestBody DeliveryScanRequest req) {
        Request order = dispatchQueue2nd.getNextOrder(req.getDeliveryZone());
        if (order != null){
            order.setStatus(RequestStatus.OUTFORDELIVERY);
            order.addInfo("\n-> out for delivery");

            Notification event = messageParser(order);
            orderEventProducer.publishOrderCreated(event);
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

        }
    }

    public void deliveredOutScan(Request request){
        OrderState state =  orderStateStore.getState(request.getRequestId());
        Request order = databaseAPI.getOrder(state.requestId());
        order.setStatus(state.status());
        order.setInfo(state.history());
        order.setDeliveryLocation(state.location());
        databaseAPI.updateOrder(order);
        order.addInfo("\n-> order delivered");
        Notification event = messageParser(order);
        orderEventProducer.publishOrderCreated(event);
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

        try{
            orderStateStore.removeState(order.getRequestId());
            order.addInfo("\n-> order deleted from cache");
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
        }catch(Exception e){
            System.out.println(e);
        }

    }

    Notification messageParser(Request order){
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