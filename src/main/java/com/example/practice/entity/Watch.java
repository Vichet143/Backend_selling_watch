package com.example.practice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "watch")
@EntityListeners(AuditingEntityListener.class)
public class Watch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String movement;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String water_resistance;
    @Column(nullable = false)
    private String case_material;
    @Column(nullable = false)
    private String strap_material;
    @Column(nullable = false)
    private String glass_type;
    @Column(nullable = false)
    private Integer warranty_month;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WatchStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
