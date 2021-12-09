package com.github.maxomys.webstore.controllers;

import com.github.maxomys.webstore.domain.Category;
import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.services.CategoryService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@Profile("mvc")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/categories")
    public String getAllCategories(Model model, Authentication authentication) {
        User activeUser = (User) authentication.getPrincipal();

        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("user", activeUser);

        return "/admin/categories";
    }

    @GetMapping("/admin/categories/new")
    public String getCategoryForm(Model model) {
        model.addAttribute("category", new Category());

        return "/categories/categoryForm";
    }

    @PostMapping("/categories")
    public String createNewCategory(@ModelAttribute("category") Category category) {
        categoryService.createNewCategory(category);

        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/{categoryId}")
    public String deleteCategoryById(@PathVariable String categoryId, Model model) {
        categoryService.deleteCategoryById(Long.valueOf(categoryId));

        return "redirect:/admin/categories";
    }

}
