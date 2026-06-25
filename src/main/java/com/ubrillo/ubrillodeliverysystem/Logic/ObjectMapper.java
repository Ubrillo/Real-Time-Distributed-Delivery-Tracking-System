package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Request and OrderState objects.
 */
@Mapper(componentModel = "spring")
public interface ObjectMapper {

    /**
     * Converts OrderState to Request domain object.
     */
    Request orderStateToRequest(OrderState state);

    /**
     * Converts Request to OrderState cache object.
     */
    OrderState requestToOrderState(Request request);

    /**
     * Copies an OrderState object into another OrderState instance.
     */
    OrderState orderStateToOrderState(OrderState state);
}

