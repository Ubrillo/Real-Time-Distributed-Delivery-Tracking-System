package com.ubrillo.ubrillodeliverysystem.Events;

import com.ubrillo.ubrillodeliverysystem.Logic.Location;
import com.ubrillo.ubrillodeliverysystem.Logic.Request;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;
import com.ubrillo.ubrillodeliverysystem.Logic.Zone;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class OrderEvent {
    @Getter
    private String customerName;
    @Getter
    private String requestId;
    @Getter
    private String description;
    @Setter
    @Getter
    private String updateAt;
    @Setter
    @Getter
    private RequestStatus status;

    @Getter
    private Zone deliveryZone;
    @Setter
    @Getter
    private Instant updatedAt;
    @Setter
    @Getter
    private Location location;
    @Setter
    @Getter
    private String history;

    @Getter
    private String userEmail;

    @Getter
    private Location destination;

    @Getter
    String deliveryAddress;

    @Getter
    String postCode;

    public OrderEvent(Request request) {
        this.customerName = request.getCustomerName();
        this.requestId = request.getRequestId();
        this.description = request.getDescription();
        this.status = request.getStatus();
        this.deliveryZone = request.getDeliveryZone();
        this.updatedAt = Instant.now();
        this.history = request.getHistory();
        this.userEmail = request.getUserEmail();
        this.destination = request.getDeliveryLocation();
        this.location = request.getCurrentLocation();
        this.deliveryAddress = request.getDeliveryAddress();
        this.postCode = request.getPostCode();
    }

    public OrderEvent(){}

}
