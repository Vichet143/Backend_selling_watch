package com.example.practice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequestDTO {

    @NotNull(message = "Watch id is required.")
    private Long watchId;
    @NotNull(message = "Quantity is required.")
    private Integer quantity;
}
