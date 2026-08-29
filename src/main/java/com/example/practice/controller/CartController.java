package com.example.practice.controller;

import com.example.practice.dto.CartRequestDTO;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.Cart;
import com.example.practice.service.CartService;
import com.example.practice.service.TelegramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final TelegramService telegramService;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> findByUserId(@PathVariable Long id){
        Cart cart = cartService.findByUserId(id);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Retrieve data with user id",
                cart
        );
        return ResponseEntity.ok().body(responseMessageDTO);
    }
}
