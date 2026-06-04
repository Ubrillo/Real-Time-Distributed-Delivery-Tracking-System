package com.ubrillo.ubrillodeliverysystem.Logic;

import jakarta.persistence.*;

import java.util.HashMap;

@Entity
public class Request {
    private String customerName;
    private String time;
    private String description;
    private String info = "";
    private String emailAddress;
    private String address;
    private String postcode;

    @Enumerated(EnumType.STRING)
    private Zone  deliveryZone;

    @Id
    private String requestId;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude"))
    })
    private Location currentLocation;

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
                   String requestId, String description,
                   Location currentLocation, Location deliveryLocation,
                   String time, String zone, String emailAdress) {

        this.customerName = customerName;
        this.requestId = requestId;
        this.description = description;
        this.currentLocation = currentLocation;
        this.deliveryLocation = deliveryLocation;
        this.time = time;
        this.status = RequestStatus.CREATED;
        this.emailAddress = emailAdress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Location getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(Location deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public void addInfo(String msg){
        info += msg;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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