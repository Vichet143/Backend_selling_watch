package com.example.practice.mapper;

import com.example.practice.dto.CategoryRequestDTO;
import com.example.practice.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequestDTO categoryRequestDTO);

    CategoryRequestDTO toCategoryRequestDto(Category category);
}
