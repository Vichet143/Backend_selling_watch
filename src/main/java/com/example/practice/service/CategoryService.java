package com.example.practice.service;

import com.example.practice.entity.Category;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CategoryService {
    Category createCategory(Category category);
    Category findById(Long id);
    Category findByName(String name);
    Category updateById(Long id, Category category);
    void deleteById(Long id);
    Page<Category> getAllCategory(Map<String,String> param);
}
