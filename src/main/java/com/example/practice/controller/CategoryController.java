package com.example.practice.controller;

import com.example.practice.dto.CategoryRequestDTO;
import com.example.practice.dto.PageDTO;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.Category;
import com.example.practice.mapper.CategoryMapperImpl;
import com.example.practice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapperImpl categoryMapper;

    @PreAuthorize("hasAnyAuthority('category:write')")
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

    @PreAuthorize("hasAnyAuthority('category:read')")
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

    @PreAuthorize("hasAuthority('category:write')")
    @PatchMapping("{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO categoryRequestDTO){
        Category category = categoryMapper.toCategory(categoryRequestDTO);
        Category category1 = categoryService.updateById(id, category);
        return ResponseEntity.ok().body(category1);
    }

    @PreAuthorize("hasAuthority('category:delete')")
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

    @PreAuthorize("hasAnyAuthority('category:read')")
    @GetMapping
    public ResponseEntity<?> getAllCategory(@RequestParam Map<String, String> param){
        Page<Category> allCategory = categoryService.getAllCategory(param);
        PageDTO pageDTO = new PageDTO(allCategory);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Retrieve data success",
                pageDTO
        );
        return ResponseEntity.ok(responseMessageDTO);
    }
}
