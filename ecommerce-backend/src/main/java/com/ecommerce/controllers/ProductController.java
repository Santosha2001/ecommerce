package com.ecommerce.controllers;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dto.Response;
import com.ecommerce.exceptions.InvalidCredentialsException;
import com.ecommerce.services.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping("/createProduct")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> createProduct(@RequestParam Long categoryId, @RequestParam MultipartFile image,
			@RequestParam String name, @RequestParam String description, @RequestParam BigDecimal price) {
		if (categoryId == null || image.isEmpty() || name.isEmpty() || description.isEmpty() || price == null) {
			throw new InvalidCredentialsException("All Fields are Required");
		}
		return ResponseEntity.ok(productService.createProduct(categoryId, image, name, description, price));
	}

	@PutMapping("/update")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> updateProduct(@RequestParam Long productId,
			@RequestParam(required = false) Long categoryId, @RequestParam(required = false) MultipartFile image,
			@RequestParam(required = false) String name, @RequestParam(required = false) String description,
			@RequestParam(required = false) BigDecimal price) {
		return ResponseEntity.ok(productService.updateProduct(productId, categoryId, image, name, description, price));
	}

	@DeleteMapping("/delete/{productId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> deleteProduct(@PathVariable Long productId) {
		return ResponseEntity.ok(productService.deleteProduct(productId));

	}

	@GetMapping("/getProductById/{productId}")
	public ResponseEntity<Response> getProductById(@PathVariable Long productId) {
		return ResponseEntity.ok(productService.getProductById(productId));
	}

	@GetMapping("/getAllProducts")
	public ResponseEntity<Response> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@GetMapping("/getProductByCategoryId/{categoryId}")
	public ResponseEntity<Response> getProductsByCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
	}

	@GetMapping("/searchProduct")
	public ResponseEntity<Response> searchForProduct(@RequestParam String searchValue) {
		return ResponseEntity.ok(productService.searchProduct(searchValue));
	}
}