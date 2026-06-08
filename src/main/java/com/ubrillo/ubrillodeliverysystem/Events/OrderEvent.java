package com.ubrillo.ubrillodeliverysystem.Events;

import com.ubrillo.ubrillodeliverysystem.Logic.Location;
import com.ubrillo.ubrillodeliverysystem.Logic.Request;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;
import com.ubrillo.ubrillodeliverysystem.Logic.Zone;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class OrderEvent {
    private String customerName;
    @Getter
    private String requestId;
    private String description;
    @Setter
    @Getter
    private String time;
    @Setter
    @Getter
    private RequestStatus status;
    private Zone deliveryZone;
    @Setter
    @Getter
    private Instant updatedAt;
    @Setter
    @Getter
    private Location location;
    @Setter
    @Getter
    private String info;
    private String userEmail;

    @Getter
    private Location destination;

    public OrderEvent(Request request) {
        this.customerName = request.getCustomerName();
        this.requestId = request.getRequestId();
        this.description = request.getDescription();
        this.time = request.getTime();
        this.status = request.getStatus();
        this.deliveryZone = request.getDeliveryZone();
        this.updatedAt = Instant.now();
        this.info = request.getInfo();
        this.userEmail = request.getEmailAddress();
        this.destination = request.getDeliveryLocation();
        this.location = request.getCurrentLocation();
    }

    public OrderEvent(){}

}
