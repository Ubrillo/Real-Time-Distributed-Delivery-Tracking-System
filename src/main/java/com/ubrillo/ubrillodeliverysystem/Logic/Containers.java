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

@Service
public class Containers{
    @Autowired
    DatabaseAPI databaseAPI;

    @Autowired
    private MessageParser messageParser;

    @Getter
    private final BlockingQueue<Request> mainQueue = new LinkedBlockingQueue<>();
    private final Map<Zone, Queue<Request>> zoneQueues = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, Request> orderList = new LinkedHashMap<>();
    private  final List<Request> batchList = new ArrayList<>();

    @Autowired
    private OrderEventProducer orderEventProducer;

    Containers(){
        for (Zone zone : Zone.values()) {
            zoneQueues.put(zone, new ConcurrentLinkedQueue<>());
        }
    }

    /*------------------ QUEUE HANDLERS ------------------ */
    public void addOrderToQueue(Request order) {
        order.setStatus(RequestStatus.DISPATCHED);
        mainQueue.add(order);
        order.addInfo("\n-> order dispatched to main queue");
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
        Notification event = messageParser.parser(order);
        orderEventProducer.publishOrderCreated(event);
    }

    public Request getOrderFromQueue(){
        Request order = mainQueue.poll();
        order.addInfo("\n->order removed from mainqueue");
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

        return order;
    }

    public Request getNextOrderFromZoneQueue(Zone zone) {
        Request request = zoneQueues.get(zone).poll();
        request.addInfo("\n-> removed from zone queue "+ zone.toString());
        return request;
    }

    public Queue<Request> getZoneQueue(Zone zone){
        return zoneQueues.get(zone);
    }

    public void addOrderToZoneQueue(Request order){
        Zone zone = order.getDeliveryZone();
        Queue<Request> zoneQueue = zoneQueues.get(zone);
        if (zoneQueue != null){
            order.setStatus(RequestStatus.OUTFORDELIVERY);
            zoneQueue.add(order);
            order.addInfo("\n-> moved to staged area: "+zone.toString());
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
            Notification event = messageParser.parser(order);
            orderEventProducer.publishOrderCreated(event);
        }
    }

    /*---------------------------- ORDER LIST ------------------- */
    public synchronized void addOrderToList(Request order){
        orderList.put(order.getRequestId(), order);
        order.addInfo("\n-> move to orderlist");
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
        Notification event = messageParser.parser(order);
        orderEventProducer.publishOrderCreated(event);
    }

    public Request getOrderFromList(String Id){
        if (orderList.containsKey(Id)){
            return orderList.get(Id);
        }
        return null;
    }

    public void removeOrderById(String id){
        System.out.println("trying to remove order");
        if (orderList.containsKey(id)) {
            Request order = getOrderFromList(id);
            order.setStatus(RequestStatus.CANCELLED);
            orderList.remove(id);
            order.addInfo("\n-> removed from orderlist");
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
            Notification event = messageParser.parser(order);
            orderEventProducer.publishOrderCreated(event);
        }
    }

    public List<Request> resetBatchList(){
        batchList.clear();
        return batchList;
    }

    public void addOrderToBatchList(Request order){
        batchList.add(order);
        order.addInfo("\n-> order added orderlist");
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
    }

}