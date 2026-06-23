package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ObjectMapper {
    Request orderStateToRequest(OrderState state);
    OrderState requestToOrderState(Request request);
    OrderState orderStateToOrderState(OrderState state);

}
