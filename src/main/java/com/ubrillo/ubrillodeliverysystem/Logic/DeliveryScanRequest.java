package com.ubrillo.ubrillodeliverysystem.Logic;

import lombok.Getter;

public class DeliveryScanRequest {
    private String deviceId;
    @Getter
    private Zone deliveryZone;
    @Getter
    private String userName;
}