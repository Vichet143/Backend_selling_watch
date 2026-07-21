package com.example.practice.service.impl;

import com.example.practice.entity.Category;
import com.example.practice.exception.ApiException;
import com.example.practice.repository.CategoryRepository;
import com.example.practice.service.CategoryService;
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
    public Category findByName(String name) {
        return categoryRepository.findCategoriesByName(name)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false" , "Category not found with name " + name));
    }

    @Override
    public Category updateById(Long id, Category category) {
        try {
            Category update = findById(id);
            if (category.getName() != null){
                update.setName(category.getName());
            }
             if (category.getDescription() != null){
                 update.setDescription(category.getDescription());
             }


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

    @Override
    public Page<Category> getAllCategory(Map<String, String> param) {
        PageFilter pageFilter = new PageFilter();

        if (param.containsKey("name")){
            pageFilter.setName(param.get("name"));
            findByName(pageFilter.getName());
        }

        if (param.containsKey("id")) {
            pageFilter.setId(Long.parseLong(param.get("id")));
            findById(pageFilter.getId());
        }

        PageSpec<Category> pageSpec = new PageSpec<>();

        pageSpec.likeIgnoreCase(
                "name",
                pageFilter.getName()
        );

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

        return categoryRepository.findAll(pageSpec, pageable);
    }

}
