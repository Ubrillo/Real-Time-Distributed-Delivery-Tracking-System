package com.ubrillo.ubrillodeliverysystem.Logic;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderState;
import com.ubrillo.ubrillodeliverysystem.WebSocket.TrackingWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class GpsService {
    Coordinate currentCoordinate;
    DeliveryDriver driver;
    TrackingWebSocketService trackingWebSocketService;

    private static Coordinate previousCoordinate = new Coordinate(-1.2835364, 51.5227032);

    @Autowired
    OrderEventProducer orderEventProducer;

    public void updateLocation(signalGPS signal){
        Coordinate currentCoordinate;
        currentCoordinate = signal.getCoordinate();

        if (currentCoordinate.longitude() != previousCoordinate.longitude() ||
                currentCoordinate.latitude() != previousCoordinate.latitude()
        ){
            for (Request order:  driver.getOrdersList()){
                order.setCurrentLocation(
                        new Location(currentCoordinate.latitude(), currentCoordinate.longitude())
                );

                orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

                trackingWebSocketService.sendTrackingUpdate(new OrderState(
                        order.getRequestId(),
                        order.getStatus(),
                        Instant.now().toString(),
                        order.getCurrentLocation(),
                        order.getDeliveryLocation(),
                        order.getInfo()
                ));
            }
        }
        previousCoordinate = currentCoordinate;
    }
}