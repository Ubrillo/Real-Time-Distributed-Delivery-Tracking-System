package com.ubrillo.ubrillodeliverysystem.Logic;


public enum RequestStatus {
    DELIVERED("Delivered"), CREATED("Created"), QUEUED("Queued"), OUTFORDELIVERY("Out-for-delivery"),
    DISPATCHED("Dispatched"), CANCELLED("Cancelled"), STAGED("Staged");
    private String status;
    private RequestStatus(String st){
        status = st;
    }
    public String toString(){
        return status;
    }
}
