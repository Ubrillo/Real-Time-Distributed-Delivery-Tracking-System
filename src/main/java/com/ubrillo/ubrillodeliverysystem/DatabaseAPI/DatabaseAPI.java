package com.ubrillo.ubrillodeliverysystem.DatabaseAPI;
import com.ubrillo.ubrillodeliverysystem.Logic.Request;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for database operations related to delivery orders.
 * <p>
 * This class acts as a wrapper around {@link DatabaseInterface} and provides
 * methods for creating, retrieving, updating, and deleting order records.
 */
@Service
public class DatabaseAPI {

    /**
     * Repository interface used to access order records in the database.
     */
    private final DatabaseInterface databaseIfz;

    /**
     * Creates a new DatabaseAPI service with the required database interface.
     *
     * @param orderDb Repository interface used for order database operations.
     */
    public DatabaseAPI(DatabaseInterface orderDb){
        this.databaseIfz = orderDb;
    }

    /**
     * Inserts a new order into the database.
     *
     * @param request Order request to be saved.
     */
    public void insertOrder(Request request){
        databaseIfz.save(request);
    }

    /**
     * Retrieves an order from the database by request ID.
     *
     * @param requestId Unique identifier of the order request.
     * @return Matching order request.
     * @throws RuntimeException If no order is found for the given request ID.
     */
    public Request getOrder(String requestId) {
        return databaseIfz.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Deletes an order from the database by request ID.
     *
     * @param requestId Unique identifier of the order request to delete.
     */
    public void deleteOrder(String requestId) {
        databaseIfz.deleteById(requestId);
    }

    /**
     * Updates the status of an existing order.
     *
     * @param requestId Unique identifier of the order request.
     * @param newStatus New status to apply to the order.
     * @throws RuntimeException If no order is found for the given request ID.
     */
    public void updateOrderStatus(String requestId, RequestStatus newStatus) {
        Request request = databaseIfz.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        request.setStatus(newStatus);
        databaseIfz.save(request);
    }

    /**
     * Updates an existing order record.
     *
     * @param request Order request containing updated information.
     * @throws RuntimeException If no order is found for the request ID.
     */
    public void updateOrder(Request request) {
        databaseIfz.findById(request.getRequestId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        databaseIfz.save(request);
    }

    /**
     * Updates the history information for an existing order.
     *
     * @param requestId Unique identifier of the order request.
     * @param info Updated order history information.
     * @throws RuntimeException If no order is found for the given request ID.
     */
    public void updateOrderInfo(String requestId, String info) {
        Request request = databaseIfz.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        request.setHistory(info);
        databaseIfz.save(request);
    }
}
