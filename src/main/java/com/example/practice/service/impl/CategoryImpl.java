package com.example.practice.service.impl;

import com.example.practice.entity.Category;
import com.example.practice.exception.ApiException;
import com.example.practice.repository.CategoryRepository;
import com.example.practice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"false", e.getMessage());
        }

    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false","Not found category at id = " + id));
    }

    @Override
    public Category updateById(Long id, Category category) {
        try {
            Category update = findById(id);
            update.setCategoryName(category.getCategoryName());
            update.setDescription(category.getDescription());

            return categoryRepository.save(update);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false","Please check all the field and fill all the field");
        }

    }

    @Override
    public void deleteById(Long id) {
        Category byId = findById(id);
        categoryRepository.deleteById(byId.getId());
    }

}
