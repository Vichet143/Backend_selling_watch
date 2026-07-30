package com.example.practice.mapper;

import com.example.practice.dto.CartItemRequestDTO;
import com.example.practice.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "cart", source = "cartId", ignore = true)
    @Mapping(target = "watch", source = "watchId", ignore = true)
    CartItem toCartItem(CartItemRequestDTO cartItemRequestDTO);
}
