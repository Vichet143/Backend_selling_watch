package com.example.practice.service.impl;

import com.example.practice.dto.WatchImageRequest;
import com.example.practice.entity.Watch;
import com.example.practice.entity.WatchImage;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.WatchImageMapper;
import com.example.practice.repository.WatchImageRepository;
import com.example.practice.service.CloudinaryService;
import com.example.practice.service.WatchImageService;
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
public class WatchImageServiceImpl implements WatchImageService {
    private final WatchService watchService;
    private final WatchImageMapper watchImageMapper;
    private final WatchImageRepository watchImageRepository;
    private final CloudinaryService cloudinaryService;
    @Override
    public WatchImage createWatchImage(WatchImageRequest watchImageRequest) {
        Watch watch = watchService.findById(watchImageRequest.getWatchId());
        String imageUrl = cloudinaryService.uploadImage(watchImageRequest.getImage_url());
        WatchImage watchImage = watchImageMapper.toWatchImage(watchImageRequest);

        watchImage.setWatch(watch);
        watchImage.setImage_url(imageUrl);

        return watchImageRepository.save(watchImage);
    }

    @Override
    public WatchImage findById(Long id) {
        return watchImageRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "Watch image not found with id " + id));
    }

    @Override
    public WatchImage updateImageById(Long id, WatchImageRequest watchImageRequest) {
        WatchImage watchImage = findById(id);
        if (watchImageRequest.getWatchId() !=null){
            Watch watch = watchService.findById(watchImageRequest.getWatchId());
            watchImage.setWatch(watch);
        }
        if (watchImageRequest.getImage_url() != null){
            String imageUrl = cloudinaryService.uploadImage(watchImageRequest.getImage_url());
            watchImage.setImage_url(imageUrl);
        }

        if (watchImageRequest.getIs_primary() != null){
            watchImage.setIs_primary(watchImageRequest.getIs_primary());
        }
        return watchImageRepository.save(watchImage);
    }

    @Override
    public void deleteById(Long id) {
        WatchImage watchImage = findById(id);
        watchImageRepository.deleteById(watchImage.getId());
    }

    @Override
    public Page<WatchImage> getAllWatchImage(Map<String, String> param) {
        PageFilter pageFilter = new PageFilter();

        if (param.containsKey("id")) {
            pageFilter.setId(Long.parseLong(param.get("id")));
            findById(pageFilter.getId());
        }
        PageSpec<WatchImage> pageSpec = new PageSpec<>();

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

        return watchImageRepository.findAll(pageSpec,pageable);
    }
}
