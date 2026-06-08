package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


public class DeliveryDriver {

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    String id;

    @Getter
    @Setter
    Location location;

    @Getter
    @Setter
    Coordinate coordinate;

    @Getter
    @Setter
    List<Request> ordersList;

    @Getter
    @Setter
    Zone deliveryZone;

    public DeliveryDriver(
            String name,
            String id,
            Location location,
            Coordinate coordinate
    ){
        this.name = name;
        this.id = id;
        this.location = location;
        this.coordinate = coordinate;
    }


    @Override
    public String toString() {
        return "DeliveryDriver{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", location=" + location +
                ", coordinate=" + coordinate +
                ", orders=" + ordersList +
                ", deliveryZone=" + deliveryZone +
                '}';
    }

    public void addToDeliveryList(Request order) {
        ordersList.add(order);
    }
}


