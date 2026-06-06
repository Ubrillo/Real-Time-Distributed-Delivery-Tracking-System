package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.List;

public record DeliveryStop(
        String orderId,
        String postcode,
        double lat,
        double lng
) {}