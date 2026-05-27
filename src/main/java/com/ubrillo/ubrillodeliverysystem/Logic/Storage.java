package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import jakarta.annotation.PostConstruct;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Storage {

    //
    private final BlockingQueue<Request> mainQueue = new LinkedBlockingQueue<>();

    // 2. Zone queues (thread-safe + scalable)
    private final Map<Zone, Queue<Request>> zoneQueues = new ConcurrentHashMap<>();

    private Map<String, Request> orderList = new LinkedHashMap<>();

    Storage(){}



    /*---------------------------- QUEUE HANDLES ------------------------------*/
    public void addOrder(Request order) {
        order.setStatus(RequestStatus.DISPATCHED);
        mainQueue.add(order);

//        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
//        Notification event = messageParser(order);
//        orderEventProducer.publishOrderCreated(event);

    }


    // 7. Drivers or services can call this
    public Request getNextOrder (Zone zone){
        return zoneQueues.get(zone).poll();
    }


    /*---------------------------- ORDER LIST HANDLES------------------------------*/

    public synchronized void addOrderX(Request order){
        orderList.put(order.getRequestId(), order);
//        order.addInfo("\n-> move to orderlist");
//        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
    }

    public synchronized List<Request> getBatch(int size){
        List<Request> batch = new ArrayList<>();
        Iterator<Request> itr = orderList.values().iterator();
        while(itr.hasNext() && batch.size() < size){
            Request order = itr.next();
            batch.add(order);
            order.addInfo("\n-> moved to mini-batch");
            //orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
            itr.remove();
            order.addInfo("\n-> removed from orderlist");
            //orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
        }
        return batch;
    }

    public Request getOrder(String Id){
        if (orderList.containsKey(Id)){
            return orderList.get(Id);
        }
        return null;
    }

    public void removeOrderById(String id){

        if (orderList.containsKey(id)) {
            Request order = getOrder(id);
            orderList.remove(id);
            order.addInfo("\n-> removed from orderlist");
            //orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
        }
    }
    public synchronized int getSize(){
        return orderList.size();
    }

    @Override
    public String toString() {
        return "OrderQueue{" +
                "orderQueue=" +  orderList +
                '}';
    }


    /*---------------------------
}
