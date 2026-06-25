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

/**
 * Core service responsible for managing order lifecycle operations,
 * including creation, cancellation, tracking, delivery, and driver assignment.
 */
@Service
public class OrderManager {

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

    private static int processedOrders = 0;

    /**
     * Default constructor.
     */
    public void OrderManager(){}

    /**
     * Creates a new order and persists it in database and system queues.
     */
    public newRequestResponse createOrder(Request request){
        Request order = requestMan.createRequest(request);
        databaseAPI.insertOrder(order);
        orderList.addOrder(order);
        batchDispatcher.checkSizeTrigger();
        System.out.println("\norder: "+ processedOrders++);
        return new newRequestResponse(order);
    }

    /**
     * Cancels an existing order.
     */
    public Request cancelOrder(Request request){
        String id = request.getRequestId();
        Request order  = orderList.getOrder(id);
        orderList.removeOrder(id);
        return order;
    }

    /**
     * Retrieves full order state, using cache first then database fallback.
     */
    public OrderState getOrderDetails(Request request){
        OrderState orderState =  orderStateStore.getState(request.getRequestId());

        if (orderState == null){
            Request order = getOrderFromDb(request);
            return mapper.requestToOrderState(order);
        }
        return orderState;
    }

    /**
     * Retrieves order object, using cache first then database fallback.
     */
    public Request getOrder(Request request){
        OrderState state =  orderStateStore.getState(request.getRequestId());

        if (state == null){
            return getOrderFromDb(request);
        }

        System.out.println(state.toString());
        return mapper.orderStateToRequest(state);
    }

    /**
     * Fetches order directly from database.
     */
    public Request getOrderFromDb(Request request){
        return databaseAPI.getOrder(request.getRequestId());
    }

    /*------------------- TRACK DRIVER -------------------*/

    /**
     * Tracks real-time driver location for a given order.
     */
    public GpsTrackingResponse trackOrderLocation(Request order){

        OrderState state = orderStateStore.getState(order.getRequestId());
        DeliveryDriver driver = driverManagement.getDriver(state.deliveryDriver());

        GpsTrackingResponse response = new GpsTrackingResponse(
                driver.currentLocation,
                state.destination()
        );

        response.setRequestId(state.requestId());
        response.setStatus(state.status());

        return response;
    }

    /**
     * Assigns a delivery driver to an order.
     */
    public void assignDeliveryDriver(String name, Request order) {
        DeliveryDriver driver = driverManagement.getDriver(name);
        driver.addToDeliveryList(order);
    }

    /*=================== DRIVER APIS =======================*/

    /**
     * Handles scan event when an order is dispatched for delivery.
     */
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

    /**
     * Publishes GPS updates for an order.
     */
    public void updateOrderGps(signalGPS signal){
        orderEventProducer.publishGpsUpdate(signal);
    }

    /**
     * Handles delivery completion scan and final order update.
     */
    public void deliveredOutScan(Request request){

        OrderState state = orderStateStore.getState(request.getRequestId());
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

    /**
     * Builds a notification message based on order status.
     */
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
