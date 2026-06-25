package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for batching and dispatching orders based on size and time triggers.
 */
@Service
public class BatchDispatcher extends Containers {

    private static final int BATCH_SIZE = 100;
    private static int batchNo = 0;

    @Autowired
    private OrderList orderList;

    @Autowired
    private DispatchQueue dispatchQueue;

    // SIZE-BASED + TIME-BASED trigger

    /**
     * Scheduled task that triggers batch processing every 10 minutes.
     */
    @Scheduled(fixedDelay = 10 * 60 * 1000) // every 10 mins
    public void timeBasedBatch() {
        processBatch();
    }

    /**
     * Triggers batch processing when order list reaches defined size threshold.
     */
    public void checkSizeTrigger() {
        if(orderList.getSize() >= BATCH_SIZE){
            processBatch();
            System.out.println("\nbatch: "+batchNo+++"\n");
        }
    }

    /**
     * Processes a batch of orders and dispatches them.
     */
    private synchronized void processBatch() {
        List<Request> batch = orderList.getBatch(BATCH_SIZE);
        if (batch.isEmpty()) return;

        for (Request order : batch) {
            dispatchQueue.addOrder(order);
            order.setStatus(RequestStatus.DISPATCHED);
        }
    }
}

