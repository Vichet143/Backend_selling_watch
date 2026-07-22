package com.example.practice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class WatchImageRequest {
    private Long watchId;
    private MultipartFile image_url;
    private Boolean is_primary;
}
