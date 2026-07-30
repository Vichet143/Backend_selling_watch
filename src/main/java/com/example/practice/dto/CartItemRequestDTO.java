package com.example.practice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequestDTO {
    @NotNull(message = "cart id is not null")
    private Long cartId;
    @NotNull(message = "watch id is not null")
    private Long watchId;
    @NotNull(message = "quantity is not null")
    private Integer quantity;
}
