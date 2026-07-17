package com.example.practice.controller;

import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.Brand;
import com.example.practice.service.BrandService;
import com.example.practice.service.CloudinaryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {
    private final CloudinaryService cloudinaryService;
    private final BrandService brandService;

    @PreAuthorize("hasAuthority('brand:write')")
    @PostMapping
    public ResponseEntity<?> createBrand( @RequestParam String name, @RequestParam String country,
            @RequestParam MultipartFile logo
    ) {

        String imageUrl = cloudinaryService.uploadImage(logo);

        Brand brand = new Brand();
        brand.setName(name);
        brand.setCountry(country);
        brand.setLogo(imageUrl);
        Brand brand1 = brandService.createBrand(brand);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Create brand success",
                brand1
        );

        return ResponseEntity.ok(responseMessageDTO);
    }
}
