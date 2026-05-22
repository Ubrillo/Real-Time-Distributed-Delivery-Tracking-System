package com.ubrillo.ubrillodeliverysystem.Logic;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderList {
    private Map<String, Request> orderList;
    public OrderList(){
        //this.order = order;
        orderList = new LinkedHashMap<>();
    }

    public synchronized void addOrder(Request order){
        orderList.put(order.getRequestId(), order);
    }

    public synchronized List<Request> getBatch(int size){
        List<Request> batch = new ArrayList<>();
        Iterator<Request> itr = orderList.values().iterator();
        while(itr.hasNext() && batch.size() < size){
            Request req = itr.next();
            batch.add(req);
            itr.remove();
        }
        return batch;
    }

    public Request getOrder(String Id){
        if (orderList.containsKey(Id)){
            return orderList.get(Id);
        }
        return null;
    }

    public void removeOrderById(String Id){
        if (orderList.containsKey(Id)) {
            orderList.remove(Id);
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