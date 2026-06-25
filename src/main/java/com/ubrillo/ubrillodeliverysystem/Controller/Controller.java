package com.ubrillo.ubrillodeliverysystem.Controller;
import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import com.ubrillo.ubrillodeliverysystem.Logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes API endpoints for users, drivers, and admins.
 * <p>
 * This controller handles order creation, cancellation, tracking,
 * GPS updates, delivery scans, and order lookup operations.
 */
@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class Controller {

    /**
     * Service responsible for managing order-related business logic.
     */
    @Autowired
    private OrderManager orderManager;

    /**
     * Simulator used for driver-related behavior during delivery tracking.
     */
    @Autowired
    private DriverSimulator sim;

    /*===========================USER API===================*/

    /**
     * Receives a new delivery request and creates an order.
     *
     * @param request Request body containing delivery order details.
     * @return Response containing the result of the order creation process.
     */
    @PostMapping("api/create-request")
    public newRequestResponse requestReceiver(@RequestBody Request request) {
        newRequestResponse response = orderManager.createOrder(request);
        return response;
    }

    /**
     * Cancels an existing delivery order.
     *
     * @param request Request body containing the order details to cancel.
     * @return Response containing the result of the cancellation process.
     */
    @PostMapping("api/cancel-request")
    public newRequestResponse cancelOrder(@RequestBody Request request) {
        return new newRequestResponse(orderManager.cancelOrder(request));
    }

//    @GetMapping("api/track-order")
//    public void trackOrder(Request order) {
//        orderManager.trackOrder(order);
//        System.out.println("calling...");
//    }

    /**
     * Retrieves order information for a user.
     *
     * @param request Request body containing the order lookup details.
     * @return Response containing the requested order information.
     */
    @GetMapping("api/view-order")
    public newRequestResponse getOrder(@RequestBody Request request) {
        return new newRequestResponse(orderManager.getOrder(request));
    }

    /**
     * Retrieves order information directly from the database.
     *
     * @param request Request body containing the order lookup details.
     * @return Request object containing order details from the database.
     */
    @GetMapping("api/view-order-db")
    public Request getOrderFromDB(@RequestBody Request request) {
        return orderManager.getOrderFromDb(request);
    }

    /**
     * Tracks the current GPS location of an order using its order ID.
     *
     * @param orderId Unique identifier of the order being tracked.
     * @return GPS tracking response containing the order location details.
     */
    @GetMapping("api/track-order/{orderId}")
    @SendTo("/gps/topic/user")
    public synchronized GpsTrackingResponse trackOrderLocation(@PathVariable String orderId) {
        Request request = new Request("", orderId, "", "", "", "");
        //System.out.println("calling...:" + orderId);
        GpsTrackingResponse response  =  orderManager.trackOrderLocation(request);
        //return orderManager.trackOrderLocation(request);
//        System.out.println(response);
        return response;
    }

    /*===================DRIVER APIS =======================*/

    /**
     * Handles the driver delivery-out scan operation.
     *
     * @param request Request body containing delivery scan details.
     */
    @PutMapping("api/driver-delivery-out-scan")
    public void deliveryOutScan(@RequestBody DeliveryScanRequest request) {
        orderManager.deliveryOutScan(request);
    }

    /**
     * Marks an order as delivered after the delivered-out scan.
     *
     * @param request Request body containing delivered order details.
     */
    @PutMapping("api/order-delivered-out-scan")
    public void deliveredOutScan(@RequestBody Request request) {
        orderManager.deliveredOutScan(request);
    }

    /**
     * Updates the GPS location signal for an active order.
     *
     * @param signal Request body containing the latest GPS signal data.
     */
    @PutMapping("api/gps.signal")
    public synchronized void updateGps(@RequestBody signalGPS signal) {
        orderManager.updateOrderGps(signal);
        //System.out.println(signal.toString());
    }

    /*=====================ADMIN APIS===================*/

    /**
     * Retrieves detailed order information for an admin user.
     *
     * @param request Request body containing the order lookup details.
     * @return Request object containing the requested order details.
     */
    @GetMapping("api/admin/view-order")
    public Request getOrderDetails(@RequestBody Request request) {
        return orderManager.getOrder(request);
    }



}
