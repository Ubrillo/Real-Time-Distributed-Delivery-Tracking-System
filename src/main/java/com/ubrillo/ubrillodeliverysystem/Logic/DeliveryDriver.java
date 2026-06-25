package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Represents a delivery driver entity responsible for managing
 * location updates and assigned delivery orders.
 */
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
    ArrayList<Location> destinations;

    @Getter
    @Setter
    Coordinate coordinate;

    @Getter
    @Setter
    List<Request> ordersList;

    @Getter
    @Setter
    Zone deliveryZone;

    /**
     * Default constructor.
     */
    public DeliveryDriver(){}

    /**
     * Constructs a delivery driver with basic identity information.
     *
     * @param name driver name
     * @param id driver identifier
     */
    public DeliveryDriver(String name, String id){
        this.name = name;
        this.id = id;
        currentLocation = new Location(GpsService.getBaseCoordinate());
        ordersList = new ArrayList<>();
    }

    /**
     * Updates current location using a Location object.
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * Updates current location using raw coordinate values.
     */
    public void setCurrentLocation(Coordinate xy){
        currentLocation = new Location(xy.latitude(), xy.longitude());
    }

    /**
     * Returns a string representation of the delivery driver state.
     */
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

    /**
     * Adds an order to the driver's delivery list.
     */
    public void addToDeliveryList(Request order) {
        ordersList.add(order);
    }

    /**
     * Moves the driver to a new geographic location.
     */
    public void moveTo(double lat, double lng) {
        this.previousLocation = this.currentLocation;
        this.currentLocation = new Location(lat, lng);
    }
}

