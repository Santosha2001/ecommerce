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

	@PostMapping("/createCategory")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> createCategory(@RequestBody CategoryDto categoryDto) {
		return ResponseEntity.ok(categoryService.createCategory(categoryDto));
	}

	@GetMapping("/getAllCategories")
	public ResponseEntity<Response> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	@PutMapping("/update/{categoryId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> updateCategory(@PathVariable Long categoryId,
			@RequestBody CategoryDto categoryDto) {
		return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDto));
	}

	@DeleteMapping("/delete/{categoryId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> deleteCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
	}

	@GetMapping("/getCategoryById/{categoryId}")
	public ResponseEntity<Response> getCategoryById(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
	}
}
