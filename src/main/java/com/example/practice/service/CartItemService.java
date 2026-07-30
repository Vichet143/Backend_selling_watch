package com.example.practice.service;

import com.example.practice.dto.CartItemRequestDTO;
import com.example.practice.entity.CartItem;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CartItemService {
    CartItem create(CartItemRequestDTO cartItemRequestDTO);
    CartItem findById(Long id);
    CartItem updateById(Long id, CartItemRequestDTO cartItemRequestDTO);
    void deleteById(Long id);
    CartItem findByCartId(Long id);
    Page<CartItem> getAllCartItem(Map<String,String > param);
}
