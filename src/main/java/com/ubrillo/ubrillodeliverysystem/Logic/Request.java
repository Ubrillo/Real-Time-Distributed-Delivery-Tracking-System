package com.ubrillo.ubrillodeliverysystem.Logic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;

/**
 * JPA entity representing a delivery request/order in the system.
 */
@Entity
public class Request {

    @Setter
    @Getter
    private String customerName;

    @Setter
    @Getter
    private Instant updateAt;

    @Setter
    @Getter
    private String description;

    @Getter
    @Setter
    private String history = "";

    @Setter
    @Getter
    private String userEmail;

    @Setter
    @Getter
    private String deliveryAddress;

    @Getter
    @Setter
    private String postCode;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private Zone deliveryZone;

    @Setter
    @Getter
    @Id
    private String requestId;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Setter
    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude"))
    })
    private Location currentLocation;

    @Setter
    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "drop_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "drop_longitude"))
    })
    private Location deliveryLocation;

    @Setter
    @Getter
    private String deliveryDriver;

    /**
     * Default constructor required by JPA.
     */
    public Request() {
        // REQUIRED
    }

    /**
     * Constructs a new delivery request with basic customer information.
     */
    public Request(String customerName,
                   String requestId,
                   String description,
                   String emailAddress,
                   String postCode,
                   String deliveryAddress
    ) {

        this.customerName = customerName;
        this.requestId = requestId;
        this.description = description;
        this.userEmail = emailAddress;
        this.postCode = postCode;
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Appends a message to the request history log.
     */
    public void addHistory(String msg){
        history += msg;
    }

    /**
     * Returns string representation of the request.
     */
    @Override
    public String toString() {
        return "Request{" +
                "customerName='" + customerName + '\'' +
                ", requestId='" + requestId + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", currentLocation=" + currentLocation +
                ", deliveryLocation=" + deliveryLocation +
                ", time='" + updateAt + '\'' +
                '}';
    }
}