package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderList extends  Containers {

    @Autowired
    private OrderEventProducer orderEventProducer;

    private Map<String, Request> orderList;
    public OrderList(){}

    public synchronized void addOrder(Request order){
        addOrderToList(order);
    }

    public synchronized List<Request> getBatch(int size){
        List<Request> batch = resetBatchList();
        Iterator<Request> itr = getOrderList().values().iterator();
        while(itr.hasNext() && batch.size() < size){
            Request order = itr.next();
            batch.add(order);
            itr.remove();
        }
        return batch;
    }

    public Request getOrder(String Id){
        return getOrderFromList(Id);
    }

    public void removeOrder(String id){
        removeOrderById(id);
    }
    public synchronized int getSize(){
        return getOrderList().size();
    }

    @Override
    public String toString() {
        return "OrderQueue{" +
                "orderQueue=" +  getOrderList() +
                '}';
    }
}