package com.example.practice.mapper;

import com.example.practice.dto.WatchImageRequest;
import com.example.practice.entity.WatchImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WatchImageMapper {

    @Mapping(target = "watch", source = "watchId", ignore = true)
    @Mapping(target = "image_url", source = "image_url", ignore = true)
    WatchImage toWatchImage(WatchImageRequest watchImageRequest);
}
