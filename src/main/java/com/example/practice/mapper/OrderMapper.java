package com.example.practice.mapper;

import com.example.practice.dto.OrderRequestDTO;
import com.example.practice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "user", source = "userId", ignore = true)
    Order toOrder(OrderRequestDTO orderRequestDTO);
}
