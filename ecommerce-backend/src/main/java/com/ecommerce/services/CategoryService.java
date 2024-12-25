package com.ecommerce.services;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.Response;

public interface CategoryService {

	Response createCategory(CategoryDto categoryRequest);

	Response updateCategory(Long categoryId, CategoryDto categoryRequest);

	Response getAllCategories();

	Response getCategoryById(Long categoryId);

	Response deleteCategory(Long categoryId);
}
