package com.ubrillo.ubrillodeliverysystem.Events;

import com.ubrillo.ubrillodeliverysystem.Logic.Location;
import com.ubrillo.ubrillodeliverysystem.Logic.Request;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;
import com.ubrillo.ubrillodeliverysystem.Logic.Zone;

import java.time.Instant;

public class OrderEvent {
    private String customerName, requestId;
    private String description, time;
    private RequestStatus status;
    private Zone deliveryZone;
    private Instant updatedAt;
    private Location location;

    public OrderEvent(Request request) {
        this.customerName = request.getCustomerName();
        this.requestId = request.getRequestId();
        this.description = request.getDescription();
        this.time = request.getTime();
        this.status = request.getStatus();
        this.deliveryZone = request.getDeliveryZone();
        this.updatedAt = Instant.now();

    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OrderEvent(){}

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Zone getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(Zone deliveryZone) {
        this.deliveryZone = deliveryZone;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
