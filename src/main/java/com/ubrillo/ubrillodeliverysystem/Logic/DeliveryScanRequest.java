package com.ubrillo.ubrillodeliverysystem.Logic;

public class DeliveryScanRequest {
    private String orderId;
    private String deviceId;
    private Zone  deliveryZone;
    private String userName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Zone getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(Zone deliveryZone) {
        this.deliveryZone = deliveryZone;
    }
}