package com.example.practice.controller;

import com.example.practice.dto.CartRequestDTO;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.Cart;
import com.example.practice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> create (@Valid  @RequestBody CartRequestDTO cartRequestDTO){
        Cart cart = cartService.create(cartRequestDTO);
        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Create cart success",
                cart
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }
}
