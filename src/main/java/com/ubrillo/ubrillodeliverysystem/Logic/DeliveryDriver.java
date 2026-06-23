package com.ubrillo.ubrillodeliverysystem.Logic;
import java.util.ArrayList;
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
    Location currentLocation;

    @Getter
    @Setter
    Location previousLocation;

    @Getter
    @Setter
    Location nextDeliveryDestination;

    @Getter
    @Setter
    ArrayList<Location>destinations;

    @Getter
    @Setter
    Coordinate coordinate;


    @Getter
    @Setter
    List<Request> ordersList;

    @Getter
    @Setter
    Zone deliveryZone;

    public DeliveryDriver(){}

    public DeliveryDriver(
            String name,
            String id,
            Location currentLocation,
            Coordinate coordinate
    ){
        this.name = name;
        this.id = id;
        this.currentLocation = currentLocation;
        this.coordinate = coordinate;
        ordersList = new ArrayList<>();
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setCurrentLocation(Coordinate xy){
        currentLocation = new Location(xy.latitude(), xy.longitude());
    }

    @Override
    public String toString() {
        return "DeliveryDriver{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", currentLocation=" + currentLocation +
                ", previousLocation=" + previousLocation +
                ", coordinate=" + coordinate +
                ", ordersList=" + ordersList +
                ", deliveryZone=" + deliveryZone +
                '}';
    }
    public void addToDeliveryList(Request order) {
        ordersList.add(order);
    }
}


