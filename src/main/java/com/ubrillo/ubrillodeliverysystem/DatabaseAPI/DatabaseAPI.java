package com.ubrillo.ubrillodeliverysystem.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Logic.Request;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DatabaseAPI {
    private final DatabaseInterface databaseIfz;
    public DatabaseAPI(DatabaseInterface orderDb){
        this.databaseIfz = orderDb;
    }

    public void insertOrder(Request request){
        databaseIfz.save(request);
    }
    public Request getOrder(String requestId) {
        return databaseIfz.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void deleteOrder(String requestId) {
        databaseIfz.deleteById(requestId);
    }

    public void updateOrderStatus(String requestId, RequestStatus newStatus) {
        Request request = databaseIfz.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        request.setStatus(newStatus);
        databaseIfz.save(request);
    }

    public void updateOrder(Request request) {
        databaseIfz.findById(request.getRequestId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        databaseIfz.save(request);
    }

    public void updateOrderInfo(String requestId, String info) {
        Request request = databaseIfz.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        request.setHistory(info);
        databaseIfz.save(request);
    }
}