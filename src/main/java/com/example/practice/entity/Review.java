package com.example.practice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "watch_id", nullable = false)
    private Watch watch;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer rating;
    private String comment;
}
