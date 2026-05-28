package com.ubrillo.ubrillodeliverysystem.Logic;
import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Queue;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class DispatchQueue2nd extends Containers implements Runnable{

    // 1. Main incoming queue (thread-safe)
   // private final Queue<Request> mainQueue = new ConcurrentLinkedQueue<>();

    //private final BlockingQueue<Request> mainQueue = new LinkedBlockingQueue<>();

    // 2. Zone queues (thread-safe + scalable)
    private final Map<Zone, Queue<Request>> zoneQueues = new ConcurrentHashMap<>();

    @Autowired
    private DatabaseAPI databaseAPI;

    private final ExecutorService dispatcherPool = Executors.newFixedThreadPool(4);   // 4 workers

    @Autowired
    private OrderEventProducer orderEventProducer;


    // 3. Called by Controller
    public void addOrder(Request order) {

        addOrderToQueue(order);

    }

//     //Background dispatcher starter
//    @PostConstruct
//    public void startDispatcher() {
//        Thread dispatcherThread = new Thread(this);
//        dispatcherThread.setDaemon(true);
//        dispatcherThread.start();
//    }
//
//     //Background loop (runs forever)
//     @Override
//     public void run() {
//         while (true) {
//             Request order = mainQueue.poll();
//
//             if (order != null) {
//                 routeOrder(order);
//             }
//             try {
//                 Thread.sleep(100); // prevents CPU overload
//             }
//              catch(InterruptedException e){
//                     Thread.currentThread().interrupt();
//                     break;
//                 }
//
//             }
//     }

    @PostConstruct
    public void startDispatcher() {
        int numberOfWorkers = 1;

        for (int i = 0; i < numberOfWorkers; i++) {
            dispatcherPool.submit(this);
        }
    }

    // 5. Background loop (runs forever)
    @Override
    public void run() {
        System.out.println("Worker started: " + Thread.currentThread().getName());
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Request order = getMainQueue().take();   // wait until order arrives

                Thread.sleep(1000);                 // simulate 1 second scan time

                routeOrder(order);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

     // 6. Routing logic
     private void routeOrder (Request order){
         addOrderToZoneQueue(order);

     }

     public Request getNextOrder (Zone zone){
        return getNextOrderFromZoneQueue(zone);
     }

     @PreDestroy
     public void shutdown () {
         dispatcherPool.shutdownNow();
     }

}