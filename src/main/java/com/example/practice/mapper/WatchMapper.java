package com.example.practice.mapper;

import com.example.practice.dto.RequestWatchDTO;
import com.example.practice.entity.Watch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WatchMapper {

    @Mapping(target = "brand", source = "brandId", ignore = true)
    @Mapping(target = "category", source = "categoryId", ignore = true)
    Watch toWatch(RequestWatchDTO requestWatchDTO);
}
