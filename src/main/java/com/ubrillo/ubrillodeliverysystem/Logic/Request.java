package com.ubrillo.ubrillodeliverysystem.Logic;

import jakarta.persistence.*;

@Entity
public class Request {
    private String customerName;
    //private String pickupAdress;
    //private String deliveryAdress;
    //private String pickupPostcode, deliveryPostcode;
    private String time;
    private String description;

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
                   String time, String zone) {

        this.customerName = customerName;
//        this.pickupAdress = pickupAdress;
//        this.deliveryAdress = deliveryAdress;
//        this.pickupPostcode = pickupPostcode;
//        this.deliveryPostcode = deliveryPostcode;
        this.requestId = requestId;
        this.description = description;
        this.currentLocation = currentLocation;
        this.deliveryLocation = deliveryLocation;
        this.time = time;
        this.status = RequestStatus.CREATED;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

//    public String getPickupAdress() {
//        return pickupAdress;
//    }
//
//    public void setPickupAdress(String pickupAdress) {
//        this.pickupAdress = pickupAdress;
//    }
//
//    public String getDeliveryAdress() {
//        return deliveryAdress;
//    }
//
//    public void setDeliveryAdress(String deliveryAdress) {
//        this.deliveryAdress = deliveryAdress;
//    }

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

//    public String getDeliveryPostcode() {
//        return deliveryPostcode;
//    }
//
//    public void setDeliveryPostcode(String deliveryPostcode) {
//        this.deliveryPostcode = deliveryPostcode;
//    }
//
//    public String getPickupPostcode() {
//        return pickupPostcode;
//    }
//
//    public void setPickupPostcode(String pickupPostcode) {
//        this.pickupPostcode = pickupPostcode;
//    }

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

    @Override
    public String toString() {
        return "Request{" +
                "customerName='" + customerName + '\'' +
//                ", pickupAdress='" + pickupAdress + '\'' +
//                ", deliveryAdress='" + deliveryAdress + '\'' +
//                ", pickupPostcode='" + pickupPostcode + '\'' +
//                ", deliveryPostcode='" + deliveryPostcode + '\'' +
                ", requestId='" + requestId + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", currentLocation=" + currentLocation +
                ", deliveryLocation=" + deliveryLocation +
                ", time='" + time + '\'' +
                '}';
    }
}