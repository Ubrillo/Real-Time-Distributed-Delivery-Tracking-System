package com.ubrillo.ubrillodeliverysystem.Logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
public class Scanner {
    private String orderId;
    private String deviceId;
    private Zone  deliveryZone;
    String type;

    @Autowired
    private DispatchQueue2nd dispatchQueue2nd;

    public Scanner() {

    }

    public void  deliveryOutScan(DeliveryScanRequest request){
        String orderId = request.getOrderId();
    }

    public void deliveredOutScan(){

    }
}
