package com.example.practice.service.impl;

import com.example.practice.dto.CartRequestDTO;
import com.example.practice.entity.Cart;
import com.example.practice.entity.User;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.CartMapper;
import com.example.practice.repository.CartRepository;
import com.example.practice.service.CartService;
import com.example.practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;
    private final UserService userService;

    @Override
    public Cart create(CartRequestDTO cartRequestDTO) {

        User user = userService.findById(cartRequestDTO.getUserId());

        if (cartRepository.findByUserId(user.getId()).isPresent()) {
            return null;
        }else {
            Cart cart = cartMapper.toCart(cartRequestDTO);
            cart.setUser(user);
            return cartRepository.save(cart);
        }

    }

    @Override
    public Cart findByUserId(Long id) {
        return cartRepository.findByUserId(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "User id not found in cart table"));
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "Not found with id " + id));
    }
}
