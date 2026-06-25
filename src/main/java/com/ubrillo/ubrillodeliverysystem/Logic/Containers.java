package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Core container class responsible for managing order storage,
 * queueing, zoning, and lifecycle transitions within the system.
 */
@Service
public class Containers {

    @Autowired
    DatabaseAPI databaseAPI;

    @Autowired
    private MessageParser messageParser;

    @Getter
    private final BlockingQueue<Request> mainQueue = new LinkedBlockingQueue<>();

    private final Map<Zone, Queue<Request>> zoneQueues = new ConcurrentHashMap<>();

    @Getter
    private final Map<String, Request> orderList = new LinkedHashMap<>();

    private final List<Request> batchList = new ArrayList<>();

    @Autowired
    private OrderEventProducer orderEventProducer;

    /**
     * Initializes zone queues for all defined zones.
     */
    Containers(){
        for (Zone zone : Zone.values()) {
            zoneQueues.put(zone, new ConcurrentLinkedQueue<>());
        }
    }

    /*------------------ QUEUE HANDLERS ------------------ */

    /**
     * Adds an order to the main processing queue and publishes related events.
     */
    public void addOrderToQueue(Request order) {
        order.setStatus(RequestStatus.DISPATCHED);
        mainQueue.add(order);
        order.addHistory("\n-> order dispatched to main queue");
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
        Notification event = messageParser.parser(order);
        orderEventProducer.publishOrderCreated(event);
    }

    /**
     * Retrieves and removes an order from the main queue.
     */
    public Request getOrderFromQueue(){
        Request order = mainQueue.poll();
        order.addHistory("\n->order removed from mainqueue");
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

        return order;
    }

    /*============================= ZONE QUEUE========================*/

    /**
     * Retrieves and removes the next order from a specific zone queue.
     */
    public Request getNextOrderFromZoneQueue(Zone zone) {
        Request order = zoneQueues.get(zone).poll();
        order.addHistory("\n-> removed from zone queue "+ zone.toString());
        return order;
    }

    /**
     * Returns the queue associated with a specific delivery zone.
     */
    public Queue<Request> getZoneQueue(Zone zone){
        return zoneQueues.get(zone);
    }

    /**
     * Adds an order to the appropriate zone queue for delivery processing.
     */
    public void addOrderToZoneQueue(Request order){
        Zone zone = order.getDeliveryZone();
        Queue<Request> zoneQueue = zoneQueues.get(zone);

        if (zoneQueue != null){
            order.setStatus(RequestStatus.OUTFORDELIVERY);
            zoneQueue.add(order);
            order.addHistory("\n-> moved to staged area: "+zone.toString());
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
        }
    }

    /*---------------------------- ORDER LIST ------------------- */

    /**
     * Adds an order to the central order list and publishes related events.
     */
    public synchronized void addOrderToList(Request order){
        orderList.put(order.getRequestId(), order);
        order.addHistory("\n-> move to orderlist");
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

        Notification event = messageParser.parser(order);
        orderEventProducer.publishOrderCreated(event);
    }

    /**
     * Retrieves an order by its ID from the order list.
     */
    public Request getOrderFromList(String Id){
        if (orderList.containsKey(Id)){
            return orderList.get(Id);
        }
        return null;
    }

    /**
     * Removes an order by ID and updates its state to CANCELLED.
     */
    public void removeOrderById(String id){
        System.out.println("trying to remove order");

        if (orderList.containsKey(id)) {
            Request order = getOrderFromList(id);
            order.setStatus(RequestStatus.CANCELLED);
            orderList.remove(id);
            order.addHistory("\n-> removed from orderlist");
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

            Notification event = messageParser.parser(order);
            orderEventProducer.publishOrderCreated(event);
        }
    }

    /**
     * Clears and resets the batch list.
     */
    public List<Request> resetBatchList(){
        batchList.clear();
        return batchList;
    }

    /**
     * Adds an order to the batch processing list.
     */
    public void addOrderToBatchList(Request order){
        batchList.add(order);
        order.addHistory("\n-> order added orderlist");
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
    }
}