package com.example.practice.service;

import com.example.practice.entity.Brand;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface BrandService {
    Brand createBrand(Brand brand);
    Brand findById(Long id);
    Brand updateBrandById(Long id, Brand brand);
    void deleteBrandById(long id);
    Page<Brand> getAllBrand(Map<String, String> param);
}
