package com.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.Response;
import com.ecommerce.services.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	/**
     * This endpoint handles the POST request to create a new category.
     * Only users with 'ADMIN' authority can access this method.
     * 
     * @param categoryDto The CategoryDto object containing the category details to be created.
     * @return ResponseEntity<Response> A response entity containing the result of the category creation operation.
     */
	@PostMapping("/createCategory")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> createCategory(@RequestBody CategoryDto categoryDto) {
		return ResponseEntity.ok(categoryService.createCategory(categoryDto));
	}

	/**
     * This endpoint handles the GET request to retrieve all categories.
     * 
     * @return ResponseEntity<Response> A response entity containing the list of all categories.
     */
	@GetMapping("/getAllCategories")
	public ResponseEntity<Response> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	/**
     * This endpoint handles the PUT request to update an existing category by its ID.
     * Only users with 'ADMIN' authority can access this method.
     * 
     * @param categoryId The ID of the category to be updated.
     * @param categoryDto The CategoryDto object containing the updated category details.
     * @return ResponseEntity<Response> A response entity containing the result of the category update operation.
     */
	@PutMapping("/update/{categoryId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> updateCategory(@PathVariable Long categoryId,
			@RequestBody CategoryDto categoryDto) {
		return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDto));
	}

	/**
     * This endpoint handles the DELETE request to delete a category by its ID.
     * Only users with 'ADMIN' authority can access this method.
     * 
     * @param categoryId The ID of the category to be deleted.
     * @return ResponseEntity<Response> A response entity containing the result of the category deletion operation.
     */
	@DeleteMapping("/delete/{categoryId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> deleteCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
	}

	/**
     * This endpoint handles the GET request to retrieve a category by its ID.
     * 
     * @param categoryId The ID of the category to be retrieved.
     * @return ResponseEntity<Response> A response entity containing the details of the requested category.
     */
	@GetMapping("/getCategoryById/{categoryId}")
	public ResponseEntity<Response> getCategoryById(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
	}
}
