package com.example.practice.mapper;

import com.example.practice.dto.ReviewRequestDTO;
import com.example.practice.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "watch", source = "watchId", ignore = true)
    @Mapping(target = "user", source = "userId", ignore = true)
    Review toReview(ReviewRequestDTO reviewRequestDTO);
}
