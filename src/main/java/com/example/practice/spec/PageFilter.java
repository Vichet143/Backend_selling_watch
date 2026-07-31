package com.example.practice.spec;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PageFilter {
    private Long id;
    private String name;
    private Long reuseId;
    private LocalDate startDate;
    private LocalDate endDate;
}
