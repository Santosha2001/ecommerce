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

	/**
     * This endpoint handles the POST request to create a new product.
     * Only users with 'ADMIN' authority can access this method.
     * 
     * @param categoryId The ID of the category to which the product belongs.
     * @param image The image file for the product.
     * @param name The name of the product.
     * @param description A description of the product.
     * @param price The price of the product.
     * @return ResponseEntity<Response> A response entity containing the result of the product creation operation.
     */
	@PostMapping("/createProduct")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> createProduct(@RequestParam Long categoryId, @RequestParam MultipartFile image,
			@RequestParam String name, @RequestParam String description, @RequestParam BigDecimal price) {
		if (categoryId == null || image.isEmpty() || name.isEmpty() || description.isEmpty() || price == null) {
			throw new InvalidCredentialsException("All Fields are Required");
		}
		
		return ResponseEntity.ok(productService.createProduct(categoryId, image, name, description, price));
	}

	/**
     * This endpoint handles the PUT request to update an existing product.
     * Only users with 'ADMIN' authority can access this method.
     * 
     * @param productId The ID of the product to be updated.
     * @param categoryId The ID of the category to which the product belongs (optional).
     * @param image The new image file for the product (optional).
     * @param name The new name of the product (optional).
     * @param description The new description of the product (optional).
     * @param price The new price of the product (optional).
     * @return ResponseEntity<Response> A response entity containing the result of the product update operation.
     */
	@PutMapping("/update")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> updateProduct(@RequestParam Long productId,
			@RequestParam(required = false) Long categoryId, @RequestParam(required = false) MultipartFile image,
			@RequestParam(required = false) String name, @RequestParam(required = false) String description,
			@RequestParam(required = false) BigDecimal price) {
		
		return ResponseEntity.ok(productService.updateProduct(productId, categoryId, image, name, description, price));
	}

	/**
     * This endpoint handles the DELETE request to delete a product by its ID.
     * Only users with 'ADMIN' authority can access this method.
     * 
     * @param productId The ID of the product to be deleted.
     * @return ResponseEntity<Response> A response entity containing the result of the product deletion operation.
     */
	@DeleteMapping("/delete/{productId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> deleteProduct(@PathVariable Long productId) {
		
		return ResponseEntity.ok(productService.deleteProduct(productId));

	}

	/**
     * This endpoint handles the GET request to retrieve a product by its ID.
     * 
     * @param productId The ID of the product to be retrieved.
     * @return ResponseEntity<Response> A response entity containing the details of the requested product.
     */
	@GetMapping("/getProductById/{productId}")
	public ResponseEntity<Response> getProductById(@PathVariable Long productId) {
		
		return ResponseEntity.ok(productService.getProductById(productId));
	}

	/**
     * This endpoint handles the GET request to retrieve all products.
     * 
     * @return ResponseEntity<Response> A response entity containing the list of all products.
     */
	@GetMapping("/getAllProducts")
	public ResponseEntity<Response> getAllProducts() {
		
		return ResponseEntity.ok(productService.getAllProducts());
	}

	/**
     * This endpoint handles the GET request to retrieve all products in a specific category.
     * 
     * @param categoryId The ID of the category to filter products by.
     * @return ResponseEntity<Response> A response entity containing the list of products in the specified category.
     */
	@GetMapping("/getProductByCategoryId/{categoryId}")
	public ResponseEntity<Response> getProductsByCategory(@PathVariable Long categoryId) {
		
		return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
	}

	/**
     * This endpoint handles the GET request to search for a product based on a search term.
     * 
     * @param searchValue The search term used to find matching products.
     * @return ResponseEntity<Response> A response entity containing the list of products that match the search term.
     */
	@GetMapping("/searchProduct")
	public ResponseEntity<Response> searchForProduct(@RequestParam String searchValue) {
		
		return ResponseEntity.ok(productService.searchProduct(searchValue));
	}
}
