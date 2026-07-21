package com.example.practice.dto;

import com.example.practice.entity.WatchStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestWatchDTO {
    private String name;
    private Long brandId;
    private Long categoryId;
    private BigDecimal price;
    private String description;
    private String model;
    private String movement;
    private String gender;
    private String water_resistance;
    private String case_material;
    private String strap_material;
    private String glass_type;
    private Integer warranty_month;
    private WatchStatus status;
}
