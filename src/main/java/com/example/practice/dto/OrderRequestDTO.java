package com.example.practice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequestDTO {
    @NotNull(message = "user id is not null")
    private long userId;
    @NotNull(message = "shipping address is not null")
    private String shippingAddress;
    @NotNull(message = "phone numberz is not null")
    private String phoneNumber;
}
