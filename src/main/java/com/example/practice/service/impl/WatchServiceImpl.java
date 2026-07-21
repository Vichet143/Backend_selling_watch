package com.example.practice.service.impl;

import com.example.practice.dto.RequestWatchDTO;
import com.example.practice.entity.Brand;
import com.example.practice.entity.Category;
import com.example.practice.entity.Watch;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.WatchMapper;
import com.example.practice.repository.WatchRepository;
import com.example.practice.service.BrandService;
import com.example.practice.service.CategoryService;
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
public class WatchServiceImpl implements WatchService {
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final WatchRepository watchRepository;
    private final WatchMapper watchMapper;

    @Override
    public Watch createWatch(RequestWatchDTO requestWatchDTO) {
        Brand brand = brandService.findById(requestWatchDTO.getBrandId());
        Category category = categoryService.findById(requestWatchDTO.getCategoryId());

        Watch watch = watchMapper.toWatch(requestWatchDTO);
        watch.setBrand(brand);
        watch.setCategory(category);
        return watchRepository.save(watch);
    }

    @Override
    public Watch findById(Long id) {

        return watchRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "Watch not foud at id = " + id));
    }

    @Override
    public Watch findByName(String name) {

        return watchRepository.findWatchByName(name)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", " Watch not found with name " + name));
    }

    @Override
    public Watch updateWatchById(Long id, RequestWatchDTO requestWatchDTO) {
        Watch watch = findById(id);
//        Watch watch1 = watchMapper.toWatch(requestWatchDTO);

        if (requestWatchDTO.getBrandId() != null){
            Brand brand = brandService.findById(requestWatchDTO.getBrandId());
            watch.setBrand(brand);
        }

        if (requestWatchDTO.getCategoryId() != null){
            Category category = categoryService.findById(requestWatchDTO.getCategoryId());
            watch.setCategory(category);
        }

        if (requestWatchDTO.getGender() != null){
            watch.setGender(requestWatchDTO.getGender());
        }

        if (requestWatchDTO.getDescription() != null){
            watch.setDescription(requestWatchDTO.getDescription());
        }

        if (requestWatchDTO.getModel() != null){
            watch.setModel(requestWatchDTO.getModel());
        }

        if (requestWatchDTO.getMovement() != null){
            watch.setMovement(requestWatchDTO.getMovement());
        }

        if (requestWatchDTO.getPrice() != null){
            watch.setPrice(requestWatchDTO.getPrice());
        }

        if (requestWatchDTO.getName() != null){
            watch.setName(requestWatchDTO.getName());
        }

        if (requestWatchDTO.getStatus() != null){
            watch.setStatus(requestWatchDTO.getStatus());
        }

        if (requestWatchDTO.getGlass_type() != null){
            watch.setGlass_type(requestWatchDTO.getGlass_type());
        }

        if (requestWatchDTO.getCase_material() != null){
            watch.setCase_material(requestWatchDTO.getCase_material());
        }

        if (requestWatchDTO.getStrap_material() != null){
            watch.setStrap_material(requestWatchDTO.getStrap_material());
        }

        if (requestWatchDTO.getWarranty_month() != null){
            watch.setWarranty_month(requestWatchDTO.getWarranty_month());
        }

        if (requestWatchDTO.getWater_resistance() != null){
            watch.setWater_resistance(requestWatchDTO.getWater_resistance());
        }
        return watchRepository.save(watch);
    }

    @Override
    public void deleteWatchById(Long id) {
        Watch watch = findById(id);
        watchRepository.deleteById(watch.getId());
    }

    @Override
    public Page<Watch> getAllWatch(Map<String, String> param) {

        PageFilter pageFilter = new PageFilter();

        if (param.containsKey("name")){
            pageFilter.setName(param.get("name"));
            findByName(pageFilter.getName());
        }
        if (param.containsKey("id")) {
            pageFilter.setId(Long.parseLong(param.get("id")));
            findById(pageFilter.getId());
        }
        PageSpec<Watch> pageSpec = new PageSpec<>();


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

        return watchRepository.findAll(pageSpec,pageable);
    }
}
