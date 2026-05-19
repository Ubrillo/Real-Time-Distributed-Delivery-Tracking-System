package com.ubrillo.ubrillodeliverysystem.Logic;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Queue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DispatchQueue2nd implements Runnable {

    // 1. Main incoming queue (thread-safe)
    private final Queue<Request> mainQueue = new ConcurrentLinkedQueue<>();

    // 2. Zone queues (thread-safe + scalable)
    private final Map<Zone, Queue<Request>> zoneQueues = new ConcurrentHashMap<>();

    // Constructor: initialize zones
    public DispatchQueue2nd() {
        for (Zone zone : Zone.values()) {
            zoneQueues.put(zone, new ConcurrentLinkedQueue<>());
        }
    }

    // 3. Called by Controller
    public void addOrder(Request order) {
        mainQueue.add(order);
    }

    // 4. Background dispatcher starter
    @PostConstruct
    public void startDispatcher() {
        Thread dispatcherThread = new Thread(this);
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
    }

    // 5. Background loop (runs forever)
    @Override
    public void run() {
        while (true) {
            Request order = mainQueue.poll();
            if (order != null) {
                routeOrder(order);
            }
            try {
                Thread.sleep(300); // prevents CPU overload
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // 6. Routing logic
    private void routeOrder(Request order) {
        Zone zone = order.getDeliveryZone();
        Queue<Request> zoneQueue = zoneQueues.get(zone);
        if (zoneQueue != null) {
            zoneQueue.add(order);
            System.out.println(order.getRequestId()+" order staged to  -> " + zone.toString());
        }
    }

    // 7. Drivers or services can call this
    public Request getNextOrder(Zone zone) {
        return zoneQueues.get(zone).poll();
        //request.setStatus(RequestStatus.OUTOFDELIVERY);
        //return request;
    }

}