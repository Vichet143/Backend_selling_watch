package com.example.practice.service;

import com.example.practice.dto.WatchImageRequest;
import com.example.practice.entity.WatchImage;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface WatchImageService {
    WatchImage createWatchImage (WatchImageRequest watchImageRequest);
    WatchImage findById( Long id);
    WatchImage updateImageById(Long id, WatchImageRequest watchImageRequest);
    void deleteById(Long id);
    Page<WatchImage> getAllWatchImage(Map<String,String> param);
}
