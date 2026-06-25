package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service responsible for managing stored orders and batch processing.
 */
@Service
public class OrderList extends Containers {

    @Autowired
    private OrderEventProducer orderEventProducer;

    private Map<String, Request> orderList;

    /**
     * Default constructor.
     */
    public OrderList(){}

    /**
     * Adds an order to the central order list.
     */
    public synchronized void addOrder(Request order){
        addOrderToList(order);
    }

    /**
     * Retrieves a batch of orders up to the specified size.
     */
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

    /**
     * Retrieves a single order by ID.
     */
    public Request getOrder(String Id){
        return getOrderFromList(Id);
    }

    /**
     * Removes an order by ID.
     */
    public void removeOrder(String id){
        removeOrderById(id);
    }

    /**
     * Returns current number of stored orders.
     */
    public synchronized int getSize(){
        return getOrderList().size();
    }

    /**
     * Returns string representation of stored orders.
     */
    @Override
    public String toString() {
        return "OrderQueue{" +
                "orderQueue=" +  getOrderList() +
                '}';
    }
}

