package com.example.practice.service.impl;

import com.example.practice.entity.Brand;
import com.example.practice.exception.ApiException;
import com.example.practice.repository.BrandRepository;
import com.example.practice.service.BrandService;
import com.example.practice.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final CloudinaryService cloudinaryService;
    @Override
    public Brand createBrand(Brand brand) {
        try{
            return brandRepository.save(brand);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false",e.getMessage());
        }
    }

    @Override
    public Brand findById(Long id) {
        return null;
    }

    @Override
    public Brand updateBrandById(Long id, Brand brand) {
        return null;
    }

    @Override
    public void deleteBrandById(long id) {

    }

    @Override
    public Page<Brand> getAllBrand(Map<String, String> param) {
        return null;
    }
}
