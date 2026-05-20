package com.ubrillo.ubrillodeliverysystem.Logic;
import com.ubrillo.ubrillodeliverysystem.DatabaseAPI.DatabaseAPI;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Queue;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class DispatchQueue2nd implements Runnable {

    // 1. Main incoming queue (thread-safe)
   // private final Queue<Request> mainQueue = new ConcurrentLinkedQueue<>();

    private final BlockingQueue<Request> mainQueue = new LinkedBlockingQueue<>();

    // 2. Zone queues (thread-safe + scalable)
    private final Map<Zone, Queue<Request>> zoneQueues = new ConcurrentHashMap<>();


    @Autowired
    private DatabaseAPI databaseAPI;

    private final ExecutorService dispatcherPool = Executors.newFixedThreadPool(4);   // 4 workers


    // Constructor: initialize zones
    public DispatchQueue2nd() {
        for (Zone zone : Zone.values()) {
            zoneQueues.put(zone, new ConcurrentLinkedQueue<>());
        }
    }

    // 3. Called by Controller
    public void addOrder(Request order) {
        mainQueue.add(order);
//        databaseAPI.updateOrderStatus(
//                order.getRequestId(),
//                RequestStatus.DISPATCHED);
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
        int numberOfWorkers = 2;

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
                Request order = mainQueue.take();   // wait until order arrives

                Thread.sleep(500);                 // simulate 1 second scan time

                routeOrder(order);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

         // 6. Routing logic
         private void routeOrder (Request order){
             Zone zone = order.getDeliveryZone();
             Queue<Request> zoneQueue = zoneQueues.get(zone);
            // System.out.println(zoneQueue);
             if (zoneQueue != null) {
                 zoneQueue.add(order);
                 order.setInfo("staged area:  "+zone.toString());

                 databaseAPI.updateOrderStatus(order.getRequestId(), RequestStatus.STAGED);

                 databaseAPI.updateOrderInfo(
                         order.getRequestId(),
                         order.getInfo()
                 );

                 System.out.println("stagingArea:" + zone.toString());
             }else{
                 System.out.println("zone is null");
             }

         }

         // 7. Drivers or services can call this
         public Request getNextOrder (Zone zone){
             return zoneQueues.get(zone).poll();
//         if (order != null)
//            databaseAPI.updateOrderStatus(order.getRequestId(), RequestStatus.DISPATCHED);
//         return order;
//        //request.setStatus(RequestStatus.OUTOFDELIVERY);
             //return request;
         }

//         @PreDestroy
//         public void shutdown () {
//             dispatcherPool.shutdownNow();
//         }

//    private void routeOrder2(Request order) {
//        Zone zone = order.getDeliveryZone();
//        Queue<Request> zoneQueue = zoneQueues.get(zone);
//        if (zoneQueue != null) { zoneQueue.add(order);
//            order.setInfo("stagingArea: "+zone.toString()); //System.out.println(order.getRequestId()+" order staged to -> " + zone.toString());
//        }
//    }

}