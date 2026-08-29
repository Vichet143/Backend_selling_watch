package com.example.practice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;

    @Column(name = "total_price")
    private BigDecimal price;

    @Column(name = "order_date", nullable = false)
    @CreatedDate
    private LocalDateTime date;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "shipping_address", nullable = false)
    private String shoppingAddress;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
}
