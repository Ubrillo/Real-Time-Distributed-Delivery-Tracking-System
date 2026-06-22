package com.ubrillo.ubrillodeliverysystem.Logic;
import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEvent;
import com.ubrillo.ubrillodeliverysystem.Events.OrderEventProducer;
import com.ubrillo.ubrillodeliverysystem.WebSocket.TrackingWebSocketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GpsService {

    @Autowired
    DriverManagement driverManagement;

    @Autowired
    TrackingWebSocketService trackingWebSocketService;

    private static Coordinate previousCoordinate = new Coordinate(-1.2835364, 51.5227032);

    @Autowired
    OrderEventProducer orderEventProducer;

    public GpsService(){}

    public void updateLocation(signalGPS signal){
        Coordinate currentCoordinate;
        currentCoordinate = signal.getCoordinate();
        DeliveryDriver driver = driverManagement.getDriver(signal.getSender());

        if (currentCoordinate.longitude() != previousCoordinate.longitude() ||
                currentCoordinate.latitude() != previousCoordinate.latitude()
        ){
            for (Request order:  driver.getOrdersList()){
                order.setCurrentLocation(
                        new Location(currentCoordinate.latitude(), currentCoordinate.longitude())
                );

                System.out.println(order.getRequestId());

                orderEventProducer.publishOrderStateTracker(new OrderEvent(order));

                ModelMapper mapper = new ModelMapper();
                trackingWebSocketService.sendTrackingUpdate(mapper.map(order, OrderState.class));
            }
            System.out.println(previousCoordinate);
            previousCoordinate = currentCoordinate;
            System.out.println(currentCoordinate);

        }
    }
}