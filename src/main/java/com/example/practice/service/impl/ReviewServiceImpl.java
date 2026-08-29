package com.example.practice.service.impl;

import com.example.practice.dto.ReviewRequestDTO;
import com.example.practice.entity.Review;
import com.example.practice.entity.User;
import com.example.practice.entity.Watch;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.ReviewMapper;
import com.example.practice.repository.ReviewRepository;
import com.example.practice.service.ReviewService;
import com.example.practice.service.UserService;
import com.example.practice.service.WatchService;
import com.example.practice.service.util.PageUtil;
import com.example.practice.spec.PageFilter;
import com.example.practice.spec.PageSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final WatchService watchService;
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    @Override
    public Review create(ReviewRequestDTO reviewRequestDTO) {
        Watch watch = watchService.findById(reviewRequestDTO.getWatchId());
        User user = userService.findById(reviewRequestDTO.getUserId());

        Review review = reviewMapper.toReview(reviewRequestDTO);
        review.setWatch(watch);
        review.setUser(user);
        review.setComment(reviewRequestDTO.getComment());
        review.setRating(reviewRequestDTO.getRating());

        return reviewRepository.save(review);
    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "review not found with id " + id));
    }

    @Override
    public Review findByWatchId(Long id) {
        return reviewRepository.findReviewByWatchId(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", " watch id not found with id " + id));
    }

    @Override
    public Page<Review> getAllData(Map<String, String> param) {
        PageFilter pageFilter = new PageFilter();

        if (param.containsKey("watchId")){
            pageFilter.setName(param.get("watchId"));
        }
        if (param.containsKey("id")) {
            pageFilter.setId(Long.parseLong(param.get("id")));
            findById(pageFilter.getId());
        }
        PageSpec<Review> pageSpec = new PageSpec<>();


        pageSpec.equalJoin("watch", "id", pageFilter.getReuseId());

        pageSpec.equal(
                "id",
                pageFilter.getId()
        );

        int pageLimit = PageUtil.DEFAULT_PAGE_LIMIT;
        if (param.containsKey( PageUtil.PAGE_LIMIT)){
            pageLimit = Integer.parseInt(param.get(PageUtil.PAGE_LIMIT));

        }

        int pageNumber = PageUtil.DEFAULT_PAGE_NUMBER;
        if (param.containsKey( PageUtil.PAGE_SIZE)){
            pageNumber = Integer.parseInt(param.get(PageUtil.PAGE_SIZE));

        }

        Pageable pageable = PageUtil.getPageable(pageNumber, pageLimit);

        return reviewRepository.findAll(pageSpec,pageable);
    }
}
