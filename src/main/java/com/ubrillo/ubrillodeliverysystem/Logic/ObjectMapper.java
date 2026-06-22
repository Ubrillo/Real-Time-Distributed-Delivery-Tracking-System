package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ObjectMapper {
    Request mapTO(OrderState state);
}
