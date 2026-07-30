package com.example.practice.service;

import com.example.practice.dto.CartRequestDTO;
import com.example.practice.entity.Cart;

public interface CartService {

    Cart create (CartRequestDTO cartRequestDTO);
    Cart findByUserId(Long id);
    Cart findById(Long id);
}
