package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.List;

public class DeliveryDriver {
    String name;
    String id;
    Location location;
    Coordinate coordinate;
    List<Request> orders;
    Zone deliveryZone;

    public DeliveryDriver(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public List<Request> getOrders() {
        return orders;
    }

    public void setOrders(List<Request> orders) {
        this.orders = orders;
    }

    public Zone getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(Zone deliveryZone) {
        this.deliveryZone = deliveryZone;
    }

    @Override
    public String toString() {
        return "DeliveryDriver{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", location=" + location +
                ", coordinate=" + coordinate +
                ", orders=" + orders +
                ", deliveryZone=" + deliveryZone +
                '}';
    }

}


