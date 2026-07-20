package com.example.practice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BrandRequestDTO {
    private String name;
    private String country;
    private MultipartFile logo;
}
