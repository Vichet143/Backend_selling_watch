package com.example.practice.service;


import com.example.practice.dto.ReviewRequestDTO;
import com.example.practice.entity.Review;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ReviewService {

    Review create (ReviewRequestDTO reviewRequestDTO);
    Review findById(Long id);
    Review findByWatchId(Long id);
    Page<Review> getAllData(Map<String, String> param);
}
