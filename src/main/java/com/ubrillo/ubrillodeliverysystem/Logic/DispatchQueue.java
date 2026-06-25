package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.Queue;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Service responsible for consuming orders from the main queue,
 * routing them into geographic zones, and dispatching them via worker threads.
 */
@Service
public class DispatchQueue extends Containers implements Runnable {

    // Zone queues (thread-safe + scalable)
    private final Map<Zone, Queue<Request>> zoneQueues = new ConcurrentHashMap<>();

    @Autowired
    private DatabaseAPI databaseAPI;

    // Thread pool responsible for background order dispatching
    private final ExecutorService dispatcherPool = Executors.newFixedThreadPool(4);

    @Autowired
    private OrderEventProducer orderEventProducer;

    /**
     * Adds an order to the main processing queue.
     */
    public void addOrder(Request order) {
        addOrderToQueue(order);
    }

    /**
     * Initializes dispatcher workers after bean construction.
     */
    @PostConstruct
    public void startDispatcher() {
        int numberOfWorkers = 1;

        for (int i = 0; i < numberOfWorkers; i++) {
            dispatcherPool.submit(this);
        }
    }

    /**
     * Main worker loop that continuously processes incoming orders.
     */
    @Override
    public void run() {
        System.out.println("Worker started: " + Thread.currentThread().getName());

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Request order = getMainQueue().take();   // waits for order
                Thread.sleep(1000);                      // simulate processing delay

                Zone zone = determineZone(order.getDeliveryLocation());
                order.setDeliveryZone(zone);

                routeOrder(order);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Routes an order to its corresponding zone queue.
     */
    private void routeOrder(Request order){
        addOrderToZoneQueue(order);
    }

    /**
     * Determines delivery zone based on coordinates relative to base location.
     */
    private Zone determineZone(Location xy){

        if (xy.getLatitude() <= GpsService.getBaseCoordinate().latitude() &&
                xy.getLongitude() >= GpsService.getBaseCoordinate().longitude()) {
            return Zone.NORTHWEST;
        }

        if (xy.getLatitude() > GpsService.getBaseCoordinate().latitude() &&
                xy.getLongitude() >= GpsService.getBaseCoordinate().longitude()) {
            return Zone.NORTHEAST;
        }

        if (xy.getLatitude() <= GpsService.getBaseCoordinate().latitude() &&
                xy.getLongitude() <= GpsService.getBaseCoordinate().longitude()) {
            return Zone.SOUTHWEST;
        }

        return Zone.SOUTHEAST;
    }

    /**
     * Retrieves the next order from a specific zone queue.
     */
    public Request getNextOrder(Zone zone){
        return getNextOrderFromZoneQueue(zone);
    }

    /**
     * Gracefully shuts down dispatcher thread pool.
     */
    @PreDestroy
    public void shutdown(){
        dispatcherPool.shutdownNow();
    }
}