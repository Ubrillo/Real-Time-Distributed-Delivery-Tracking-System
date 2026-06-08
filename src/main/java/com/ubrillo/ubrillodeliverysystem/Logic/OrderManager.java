package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderState;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderStateStore;
import com.ubrillo.ubrillodeliverysystem.WebSocket.TrackingWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    DriverManagement driverManagement;


    public void OrderManager(){}


    public newRequestResponse createOrder(Request request){
        Request order = requestMan.createRequest(request);
        databaseAPI.insertOrder(order);
        orderList.addOrder(order);
        batchDispatcher.checkSizeTrigger();
        return new newRequestResponse(order);
    }

    public void cancelOrder(Request request){
        String id = request.getRequestId();
        Request order  = orderList.getOrder(id);
        orderList.removeOrder(id);
    }

    public OrderState getOrder(Request request){
        OrderState state =  orderStateStore.getState(request.getRequestId());

        //if order not found in cache, check database
        if (state == null){
            Request order = getOrderFromDb(request);
            return new OrderState(
                    order.getRequestId(),
                    order.getStatus(),
                    order.getTime(),
                    request.getCurrentLocation(),
                    request.getDeliveryLocation(),
                    request.getInfo()
            );
        }
        return state;
    }



    public Request  getOrderFromDb(Request request){
        return databaseAPI.getOrder(request.getRequestId());
    }

    public OrderState trackOrder(Request order){
        return getOrder(order);
    }

    public void assignDeliveryDriver(String name, Request order) {
        DeliveryDriver driver = driverManagement.getDriver(name);
//        if (order == null) {
//            do {
//                order = dispatchQueue2nd.getNextOrder(zone);
//                driver.addToDeliveryList(order);
//            } while (order != null);
//        }
        driver.addToDeliveryList(order);
    }

    /*===================DRIVER APIS =======================*/
    public void deliveryOutScan(@RequestBody DeliveryScanRequest req) {
        Request order = dispatchQueue2nd.getNextOrder(req.getDeliveryZone());
        if (order != null){
            order.setStatus(RequestStatus.OUTFORDELIVERY);
            order.addInfo("\n-> out for delivery");

            assignDeliveryDriver(req.getUserName(), order);

            Notification event = messageParser(order);
            orderEventProducer.publishOrderCreated(event);
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
        }
    }

    public void updateOrderGps(signalGPS signal){
        orderEventProducer.publishGpsUpdate(signal);

    }

    public void deliveredOutScan(Request request){
        OrderState state =  orderStateStore.getState(request.getRequestId());
        Request order = databaseAPI.getOrder(state.requestId());
        order.setStatus(RequestStatus.DELIVERED);
        order.setInfo("Delivered");
        order.setDeliveryLocation(state.location());
        order.setTime(requestMan.getCurrentTime());
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
        String recipient = order.getCustomerName();
        String description = order.getDescription();
        String time = requestMan.getCurrentTime();
        String orderId = order.getRequestId();
        RequestStatus orderStatus = order.getStatus();

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
                orderId,
                message,
                order.getEmailAddress(),
                orderStatus
        );
    }
}