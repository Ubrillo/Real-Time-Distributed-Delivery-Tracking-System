package com.ubrillo.ubrillodeliverysystem.Logic;

public class newRequestResponse{
    private String customerName, requestId; //deliveryAdress, postAddress;
    private String description, time;
    private RequestStatus status;
    private Zone deliveryZone;

    public newRequestResponse(Request request) {
        this.customerName = request.getCustomerName();
        this.requestId = request.getRequestId();
        this.description = request.getDescription();
        this.time = request.getTime();
        this.status = request.getStatus();
        this.deliveryZone = request.getDeliveryZone();

        //this.deliveryAdress = request.getDeliveryAdress();
        //this.postAddress = request.getPickupAdress();
    }

//    public void newRequestResponse() {
//
//    }

//    public String getPostAddress() {
//        return postAddress;
//    }
//
//    public void setPostAddress(String postAddress) {
//        this.postAddress = postAddress;
//    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

//
//    public String getDeliveryAdress() {
//        return deliveryAdress;
//    }

//    public void setDeliveryAdress(String deliveryAdress) {
//        this.deliveryAdress = deliveryAdress;
//    }

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
}
