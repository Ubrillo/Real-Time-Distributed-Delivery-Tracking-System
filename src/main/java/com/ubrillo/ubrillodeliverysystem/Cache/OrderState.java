package com.ubrillo.ubrillodeliverysystem.Cache;

import com.ubrillo.ubrillodeliverysystem.Logic.Location;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents the current state of a delivery order request.
 * <p>
 * This record stores identifying details, delivery status, location information,
 * customer details, driver assignment, and order history.
 *
 * @param requestId Unique identifier for the delivery request.
 * @param status Current status of the delivery request.
 * @param updatedAt Timestamp indicating when the order state was last updated.
 * @param location Current location associated with the order.
 * @param destination Destination location for the delivery.
 * @param customerName Name of the customer who placed the order.
 * @param description Description or additional details about the delivery request.
 * @param userEmail Email address of the user associated with the request.
 * @param deliveryAddress Full delivery address for the order.
 * @param postCode Postal code for the delivery address.
 * @param deliveryDriver Name or identifier of the assigned delivery driver.
 * @param history Historical record of updates made to the order state.
 */
public record OrderState(
        String requestId,
        RequestStatus status,
        Instant updatedAt,
        Location location,
        Location destination,
        String customerName,
        String description,
        String userEmail,
        String deliveryAddress,
        String postCode,
        String deliveryDriver,
        String history

) implements Serializable {

}