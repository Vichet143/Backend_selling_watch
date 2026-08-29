package com.example.practice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequestDTO {

    @NotNull(message = "watch id is required")
    private Long watchId;
    @NotNull(message = "user id is required")
    private Long userId;
    @NotNull(message = "rating is required")
    private Integer rating;
    private String comment;
}
