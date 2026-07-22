package com.example.practice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "watch_image")
public class WatchImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "watch_id" , nullable = false)
    private Watch watch;


    private String image_url;
    private Boolean is_primary;
}
