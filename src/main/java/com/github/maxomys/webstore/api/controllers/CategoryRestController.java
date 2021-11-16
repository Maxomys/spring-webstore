package com.github.maxomys.webstore.api.controllers;

import lombok.RequiredArgsConstructor;
import com.github.maxomys.webstore.api.dtos.CategoryDto;
import com.github.maxomys.webstore.api.mappers.CategoryMapper;
import com.github.maxomys.webstore.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {
    
    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getCategories().stream()
            .map(categoryMapper::categoryToCategoryDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        return categoryMapper.categoryToCategoryDto(categoryService.findById(categoryId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.createNewCategory(categoryMapper.categoryDtoToCategory(categoryDto));
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategoryById(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }

}
