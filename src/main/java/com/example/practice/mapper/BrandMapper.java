package com.example.practice.mapper;

import com.example.practice.dto.BrandRequestDTO;
import com.example.practice.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(source = "logo",target = "logo", ignore = true)
    Brand toBrand(BrandRequestDTO brandRequestDTO);
}
