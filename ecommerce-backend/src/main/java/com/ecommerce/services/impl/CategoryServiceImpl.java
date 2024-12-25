package com.ecommerce.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.Response;
import com.ecommerce.entities.Category;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.mappers.EntityDtoMapper;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.services.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepo;
	private final EntityDtoMapper entityDtoMapper;

	@Override
	public Response createCategory(CategoryDto categoryRequest) {
		Category category = new Category();
		category.setName(categoryRequest.getName());
		categoryRepo.save(category);
		
		return Response.builder().status(200)
				.message("Category created successfully").build();
	}

	@Override
	public Response updateCategory(Long categoryId, CategoryDto categoryRequest) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new NotFoundException("Category Not Found"));
		category.setName(categoryRequest.getName());
		categoryRepo.save(category);
		
		return Response.builder().status(200)
				.message("category updated successfully").build();
	}

	@Override
	public Response getAllCategories() {
		List<Category> categories = categoryRepo.findAll();
		List<CategoryDto> categoryDtoList = categories
				.stream()
				.map(entityDtoMapper::mapCategoryToDtoBasic)
				.collect(Collectors.toList());

		return Response.builder().status(200)
				.categoryList(categoryDtoList).build();
	}

	@Override
	public Response getCategoryById(Long categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new NotFoundException("Category Not Found"));
		CategoryDto categoryDto = entityDtoMapper.mapCategoryToDtoBasic(category);
		
		return Response.builder().status(200).category(categoryDto).build();
	}

	@Override
	public Response deleteCategory(Long categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new NotFoundException("Category Not Found"));
		categoryRepo.delete(category);
		
		return Response.builder().status(200)
				.message("Category was deleted successfully").build();
	}
}
