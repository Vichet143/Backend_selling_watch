package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequestDTO {
    @JsonProperty("user_id")
    @NotNull(message = "User id is required.")
    private Long userId;
}
