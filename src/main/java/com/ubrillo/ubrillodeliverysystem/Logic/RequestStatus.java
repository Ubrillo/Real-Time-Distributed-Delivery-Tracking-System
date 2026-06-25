
package com.ubrillo.ubrillodeliverysystem.Logic;

/**
 * Enum representing the lifecycle status of a delivery request.
 */
public enum RequestStatus {

    DELIVERED("Delivered"),
    CREATED("Created"),
    QUEUED("Queued"),
    OUTFORDELIVERY("Out-for-delivery"),
    DISPATCHED("Dispatched"),
    CANCELLED("Cancelled"),
    STAGED("Staged");

    private String status;

    /**
     * Constructs enum value with human-readable status string.
     */
    private RequestStatus(String st){
        status = st;
    }

    /**
     * Returns human-readable string representation of the status.
     */
    @Override
    public String toString(){
        return status;
    }
}

