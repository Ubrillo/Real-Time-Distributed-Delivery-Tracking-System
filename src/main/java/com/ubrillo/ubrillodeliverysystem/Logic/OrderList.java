package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderList {

    @Autowired
    private OrderEventProducer orderEventProducer;

    private Map<String, Request> orderList;
    public OrderList(){
        //this.order = order;
        orderList = new LinkedHashMap<>();
    }

    public synchronized void addOrder(Request order){
        orderList.put(order.getRequestId(), order);
        order.addInfo("\n-> move to orderlist");
        orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
    }

    public synchronized List<Request> getBatch(int size){
        List<Request> batch = new ArrayList<>();
        Iterator<Request> itr = orderList.values().iterator();
        while(itr.hasNext() && batch.size() < size){
            Request order = itr.next();
            batch.add(order);
            order.addInfo("\n-> moved to mini-batch");
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
            itr.remove();
            order.addInfo("\n-> removed from orderlist");
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
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
            orderEventProducer.publishOrderStateTracker(new OrderEvent(order));
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
}