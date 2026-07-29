package com.example.practice.mapper;

import com.example.practice.dto.CartRequestDTO;
import com.example.practice.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "user", ignore = true)
    Cart toCart(CartRequestDTO cartRequestDTO);
}
