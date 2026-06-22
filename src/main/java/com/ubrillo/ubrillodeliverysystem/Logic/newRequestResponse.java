package com.ubrillo.ubrillodeliverysystem.Logic;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class newRequestResponse{
    @Getter
    private String customerName;
    @Getter
    @Setter
    private String requestId;

    @Getter
    @Setter
    private Instant updateAt;
    @Getter
    @Setter
    private RequestStatus status;

    @Setter
    @Getter
    private String deliveryAddress;

    @Getter
    @Setter
    private String postCode;

    @Getter
    @Setter
    private String userEmail;

    @Setter
    @Getter
    private String description;

    public newRequestResponse(Request request) {
        this.customerName = request.getCustomerName();
        this.requestId = request.getRequestId();
        this.updateAt = request.getUpdateAt();
        this.status = request.getStatus();
        this.deliveryAddress = request.getDeliveryAddress();
        this.postCode = request.getPostCode();
        this.userEmail = request.getUserEmail();
        this.description = request.getDescription();
    }

}
