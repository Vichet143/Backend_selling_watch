package com.example.practice.spec;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Data
public class CategoryFilter {
    private Long id;
    private String categoryName;
//    private String description;
//    private LocalDate createdAt;
//    private LocalDate updatedAt;
}
