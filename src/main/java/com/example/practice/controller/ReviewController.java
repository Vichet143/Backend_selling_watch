package com.example.practice.controller;

import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.dto.ReviewRequestDTO;
import com.example.practice.entity.Review;
import com.example.practice.exception.ApiException;
import com.example.practice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO){

        try{
            Review review = reviewService.create(reviewRequestDTO);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Rating is success",
                    review
            );

            return ResponseEntity.ok().body(responseMessageDTO);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam Map<String, String> param){
        Page<Review> allData = reviewService.getAllData(param);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Retrieve data success",
                allData
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }
}
