package com.example.practice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailRequestDTO {

    @NotNull(message = "order id is not null")
    private Long orderId;
    @NotNull(message = "watch id is not null")
    private Long watchId;
    @NotNull(message = "quantity is not null")
    private Integer quantity;
    @NotNull(message = "price is not null")
    private BigDecimal price;
}
