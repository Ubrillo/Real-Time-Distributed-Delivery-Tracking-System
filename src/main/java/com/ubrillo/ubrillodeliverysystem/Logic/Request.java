package com.ubrillo.ubrillodeliverysystem.Logic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Entity
public class Request {
    @Setter
    @Getter
    private String customerName;
    @Setter
    @Getter
    private String time;
    @Setter
    @Getter
    private String description;
    @Getter
    @Setter
    private String info = "";
    @Setter
    @Getter
    private String emailAddress;
    private String deliveryAddress;
    @Getter
    private String postCode;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private Zone  deliveryZone;

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

    public Request() {
        // REQUIRED
    }

    public Request(String customerName,
                   String requestId,
                   String description,
                   Location currentLocation,
                   Location deliveryLocation,
                   String time,
                   String emailAddress,
                   String postCode) {

        this.customerName = customerName;
        this.requestId = requestId;
        this.description = description;
        this.currentLocation = currentLocation;
        this.deliveryLocation = deliveryLocation;
        this.time = time;
        this.status = RequestStatus.CREATED;
        this.emailAddress = emailAddress;
        this.postCode = postCode;
    }

    public void addInfo(String msg){
        info += msg;
    }


    @Override
    public String toString() {
        return "Request{" +
                "customerName='" + customerName + '\'' +

                ", requestId='" + requestId + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", currentLocation=" + currentLocation +
                ", deliveryLocation=" + deliveryLocation +
                ", time='" + time + '\'' +
                '}';
    }
}