package com.example.practice.service;

import com.example.practice.entity.Category;

public interface CategoryService {
    Category createCategory(Category category);
    Category findById(Long id);
    Category updateById(Long id, Category category);
    void deleteById(Long id);
}
