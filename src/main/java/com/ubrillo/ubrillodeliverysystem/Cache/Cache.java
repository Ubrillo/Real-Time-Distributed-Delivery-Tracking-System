package com.ubrillo.ubrillodeliverysystem.Cache;

public interface Cache {
    public OrderState getState(String requestId);
}
