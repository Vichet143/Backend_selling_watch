package com.example.practice.controller;

import com.example.practice.dto.CategoryRequestDTO;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.Category;
import com.example.practice.mapper.CategoryMapperImpl;
import com.example.practice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapperImpl categoryMapper;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO){
        Category categoryRequestDTO1 = categoryMapper.toCategory(categoryRequestDTO);
        Category category = categoryService.createCategory(categoryRequestDTO1);
        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Create data success",
                category
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id){
        Category byId = categoryService.findById(id);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Data retrieve success",
                byId
        );
        return ResponseEntity.ok().body(responseMessageDTO);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO categoryRequestDTO){
        Category category = categoryMapper.toCategory(categoryRequestDTO);
        Category category1 = categoryService.updateById(id, category);
        return ResponseEntity.ok().body(category1);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Category deleted successfully",
                id
        );
        return ResponseEntity.ok(responseMessageDTO);
    }
}
