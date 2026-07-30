package com.example.practice.controller;

import com.example.practice.dto.CartItemRequestDTO;
import com.example.practice.dto.PageDTO;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.CartItem;
import com.example.practice.exception.ApiException;
import com.example.practice.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cartitem")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PreAuthorize("hasAuthority('cartItem:write')")
    @PostMapping
    public ResponseEntity<?> create (@Valid @RequestBody CartItemRequestDTO cartItemRequestDTO){
        try{
            CartItem cartItem = cartItemService.create(cartItemRequestDTO);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Cart Item Create Success",
                    cartItem
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('cartItem:write')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody CartItemRequestDTO cartItemRequestDTO){

        try{
            CartItem cartItem = cartItemService.updateById(id, cartItemRequestDTO);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Update cart item success",
                    cartItem
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('cartItem:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){

        try{
            cartItemService.deleteById(id);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Delete success",
                    "delete with id " + id
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        }catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('cartItem:read')")
    @GetMapping
    public ResponseEntity<?> getAllCartItem(@RequestParam Map<String, String> param){
        try{
            Page<CartItem> allCartItem = cartItemService.getAllCartItem(param);

            PageDTO pageDTO = new PageDTO(allCartItem);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Get data cart item",
                    pageDTO
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        }catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

}
