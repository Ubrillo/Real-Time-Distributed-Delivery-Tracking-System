package com.ubrillo.ubrillodeliverysystem.Logic;

public class newRequestResponse{
    private String customerName, requestId, deliveryAdress, postAddress;
    private String description, time;

    public newRequestResponse(Request request) {
        this.customerName = request.getCustomerName();
        this.requestId = request.getRequestId();
        //this.deliveryAdress = request.getDeliveryAdress();
        this.description = request.getDescription();
        this.time = request.getTime();
        //this.postAddress = request.getPickupAdress();
    }

//    public void newRequestResponse() {
//
//    }

    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTrackId() {
        return requestId;
    }

    public void setTrackId(String requestId) {
        this.requestId = requestId;
    }

    public String getDeliveryAdress() {
        return deliveryAdress;
    }

    public void setDeliveryAdress(String deliveryAdress) {
        this.deliveryAdress = deliveryAdress;
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
}
