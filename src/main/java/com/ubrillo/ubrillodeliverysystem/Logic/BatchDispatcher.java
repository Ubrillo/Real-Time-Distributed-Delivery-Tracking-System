//package com.ubrillo.ubrillodeliverysystem.Logic;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class BatchDispatcher {
//    private static final int BATCH_SIZE = 5;
//
//    @Autowired
//    private OrderList orderList;
//
//    @Autowired
//    private DispatchQueue2nd dispatchQueue2nd;
//
//    // SIZE-BASED + TIME-BASED trigger
//    @Scheduled(fixedDelay = 10*60*1000) //every 10mins
//    public void timeBasedBatch(){
//        processBatch();
//    }
//
//    public void checkSizeTrigger(){
//        if (orderList.getSize() >= BATCH_SIZE){
//            processBatch();
//        }
//    }
//
//    private synchronized void processBatch(){
//        List<Request> batch = orderList.getBatch(BATCH_SIZE);
//        if (batch.isEmpty()) return;
//
//        for (Request order: batch){
//            dispatchQueue2nd.addOrder(order);
//        }
//        System.out.println("Batch moved: " + batch.size());
//    }
//}