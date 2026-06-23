package com.ubrillo.ubrillodeliverysystem.Logic;
import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import com.ubrillo.ubrillodeliverysystem.WebSocket.TrackingWebSocketService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GpsService {

    @Autowired
    DriverManagement driverManagement;

    @Autowired
    TrackingWebSocketService trackingWebSocketService;

    @Getter
    private static Coordinate baseCoordinate = new Coordinate(-0.1281105, 51.507458);

    @Autowired
    OrderEventProducer orderEventProducer;

    public GpsService(){}

    public void updateLocation(signalGPS signal){

        Coordinate currentCoordinate;

        currentCoordinate = signal.getCoordinate();

        DeliveryDriver driver = driverManagement.getDriver(signal.getSender());

        Location previousLocation = driver.previousLocation;

        if (currentCoordinate.longitude() != previousLocation.getLongitude() ||
                currentCoordinate.latitude() != previousLocation.getLatitude()
        ){
            driver.setCurrentLocation(currentCoordinate);

            for (Request order:  driver.getOrdersList()){

//                order.setCurrentLocation(
//                        new Location(currentCoordinate.latitude(), currentCoordinate.longitude())
//                );


                //orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

//                trackingWebSocketService.sendTrackingUpdate(new OrderState(
//                        order.getRequestId(),
//                        order.getStatus(),
//                        order.getUpdateAt(),
//                        order.getCurrentLocation(),
//                        order.getDeliveryLocation(),
//                        order.getCustomerName(),
//                        order.getDescription(),
//                        order.getUserEmail(),
//                        order.getDeliveryAddress(),
//                        order.getPostCode(),
//                        order.getDeliveryDriver(),
//                        order.getHistory()
//                ));
                double lat, lon;
                lat = currentCoordinate.latitude();
                lon = currentCoordinate.longitude();
                GpsTrackingResponse response = GpsTrackingResponse.builder().
                        currentLocation(new Location(lat, lon)).
                        destination(order.getDeliveryLocation()).
                        status(order.getStatus()).
                        build();
                trackingWebSocketService.sendTrackingUpdate(response);
            }
        }
    }



}