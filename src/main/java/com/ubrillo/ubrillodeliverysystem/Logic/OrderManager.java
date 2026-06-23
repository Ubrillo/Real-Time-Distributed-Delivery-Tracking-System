package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import com.ubrillo.ubrillodeliverysystem.Cache.CacheLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Service
public class OrderManager{
    @Autowired
    RequestService requestMan;

    @Autowired
    OrderList orderList;

    @Autowired
    DispatchQueue dispatchQueue;

    @Autowired
    CacheLogic orderStateStore;

    @Autowired
    BatchDispatcher batchDispatcher;

    @Autowired
    DatabaseAPI databaseAPI;

    @Autowired
    OrderEventProducer orderEventProducer;

    @Autowired
    DriverManagement driverManagement;

    @Autowired
    private ObjectMapper mapper;



    public void OrderManager(){}


    public newRequestResponse createOrder(Request request){
        Request order = requestMan.createRequest(request);
        databaseAPI.insertOrder(order);
        orderList.addOrder(order);
        batchDispatcher.checkSizeTrigger();
        return new newRequestResponse(order);
    }

    public Request cancelOrder(Request request){
        String id = request.getRequestId();
        Request order  = orderList.getOrder(id);
        orderList.removeOrder(id);
        return order;
    }

    public OrderState getOrderDetails(Request request){
        OrderState orderState =  orderStateStore.getState(request.getRequestId());
        //if order not found in cache, check database
        if (orderState == null){
            Request order = getOrderFromDb(request);

            return mapper.requestToOrderState(order);
        }
        return orderState;
    }

    public Request getOrder(Request request){
        OrderState state =  orderStateStore.getState(request.getRequestId());
        //if order not found in cache, check database
        if (state == null){
            return getOrderFromDb(request);

        }
        System.out.println(state.toString());
        return mapper.orderStateToRequest(state);
    }

    public Request  getOrderFromDb(Request request){
        return databaseAPI.getOrder(request.getRequestId());
    }

    public  GpsTrackingResponse trackOrderLocation(Request order){

        OrderState state = orderStateStore.getState(order.getRequestId());
        //return state;
        DeliveryDriver driver =  driverManagement.getDriver(state.deliveryDriver());

        GpsTrackingResponse response = new GpsTrackingResponse(
                driver.currentLocation,
                state.destination()
        );
        response.setRequestId(state.requestId());
        response.setStatus(state.status());
        return response;
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
    public void deliveryOutScan(@RequestBody DeliveryScanRequest payload) {
        Request order = dispatchQueue.getNextOrder(payload.getDeliveryZone());
        if (order != null){
            order.setStatus(RequestStatus.OUTFORDELIVERY);
            order.addHistory("\n-> out for delivery");

            order.setDeliveryDriver(payload.getUserName());
            assignDeliveryDriver(payload.getUserName(), order);

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
        order.setHistory("Delivered");
        order.setDeliveryLocation(state.location());
        order.setUpdateAt(Instant.now());
        databaseAPI.updateOrder(order);
        order.addHistory("\n-> order delivered");
        Notification event = messageParser(order);
        orderEventProducer.publishOrderCreated(event);
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

        try{
            orderStateStore.removeState(order.getRequestId());
            order.addHistory("\n-> order deleted from cache");
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
                order.getUserEmail(),
                orderStatus
        );
    }
}