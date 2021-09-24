package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getCategories();

    Category createNewCategory(Category category);

    void deleteCategoryById(Long id);

}
