package com.example.practice.mapper;

import com.example.practice.dto.OrderDetailRequestDTO;
import com.example.practice.entity.OrderDetail;
import org.aspectj.apache.bcel.generic.TABLESWITCH;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    @Mapping(target = "order", source = "orderId", ignore = true)
    @Mapping(target = "watch", source = "watchId", ignore = true)
    OrderDetail toOrderDetail(OrderDetailRequestDTO orderDetailRequestDTO);
}
