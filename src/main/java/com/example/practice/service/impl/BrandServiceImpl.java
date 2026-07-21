package com.example.practice.service.impl;

import com.example.practice.entity.Brand;
import com.example.practice.exception.ApiException;
import com.example.practice.repository.BrandRepository;
import com.example.practice.service.BrandService;
import com.example.practice.service.CloudinaryService;
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
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Brand createBrand(Brand brand) {
        try {
            return brandRepository.save(brand);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }
    }

    @Override
    public Brand findById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "Brand not found " + id));
    }

    @Override
    public Brand finByName(String name) {
        return brandRepository.findBrandsByName(name)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "Brand not found with name " + name));
    }

    @Override
    public Brand updateBrandById(Long id, Brand brand) {
        Brand brand1 = findById(id);
        if (brand.getName() != null) {
            brand1.setName(brand.getName());
        }

        if (brand.getCountry() != null) {
            brand1.setCountry(brand.getCountry());
        }

        if (brand.getLogo() != null) {
            brand1.setLogo(brand.getLogo());
        }
        return brandRepository.save(brand1);

    }

    @Override
    public void deleteBrandById(long id) {
        Brand byId = findById(id);
        brandRepository.deleteById(byId.getId());
    }

    @Override
    public Page<Brand> getAllBrand(Map<String, String> param) {
        PageFilter pageFilter = new PageFilter();

        if (param.containsKey("name")){
            pageFilter.setName(param.get("name"));
            finByName(pageFilter.getName());
        }
        if (param.containsKey("id")) {
            pageFilter.setId(Long.parseLong(param.get("id")));
            findById(pageFilter.getId());
        }
        PageSpec<Brand> pageSpec = new PageSpec<>();

        pageSpec.likeIgnoreCase(
                "name",
                pageFilter.getName()
        );

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

        return brandRepository.findAll(pageSpec, pageable);
    }
}
