package com.example.CTT_shops.service.category;

import java.util.List;

import com.example.CTT_shops.model.Category;

public interface ICategoryService {
    Category getCategoryById(long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    void deleteCategoryById(Long id);
}
