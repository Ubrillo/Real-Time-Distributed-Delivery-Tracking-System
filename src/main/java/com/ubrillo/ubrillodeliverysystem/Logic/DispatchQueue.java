/*
package com.ubrillo.ubrillodeliverysystem.Logic;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DispatchQueue{
    private Queue<Request> mainQueue;
    private Queue<Request> queueNorthLondon;
    private Queue<Request> queueCentralLondon;
    private Queue<Request> queueWestLondon;
    private Queue<Request> queueEastLondon;
    private Queue<Request> queueSouthLondon;
    private Zone zone;

    Map<Zone, Queue<Request>> zoneQueues = new HashMap<>();

    public DispatchQueue(){
        mainQueue = new LinkedList<>();
        queueSouthLondon = new LinkedList<>();
        queueCentralLondon = new LinkedList<>();
        queueNorthLondon = new LinkedList<>();
        queueEastLondon = new LinkedList<>();
    }

    public void addOrder(Request order){
        mainQueue.add(order);
        zoneQueues.get(order.getDeliveryZone()).add(order);
    }

    public Request getOrder(){
        return mainQueue.poll();
    }

    public void sorter(){
        while (!mainQueue.isEmpty()){
            Request order = mainQueue.poll();
            switch (order.getDeliveryZone()){
                case Zone.WESTLONDON:
                    queueWestLondon.add(order);
                    break;
                case Zone.EASTLONDON:
                    queueEastLondon.add(order);
                    break;
                case Zone.NORTHLONDON:
                    queueNorthLondon.add(order);
                    break;
                case Zone.SOUTHLONDON:
                    queueSouthLondon.add(order);
                    break;
            }
        }
    }
    private void routeToZone(Request order) {
        switch (order.getDeliveryZone()) {
            case Zone.NORTHLONDON -> queueNorthLondon.add(order);
            case Zone.SOUTHLONDON -> queueSouthLondon.add(order);
            case Zone.EASTLONDON -> queueEastLondon.add(order);
            case Zone.WESTLONDON -> queueWestLondon.add(order);
        }
    }
}


*/
