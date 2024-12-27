package com.ecommerce.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.Response;
import com.ecommerce.entities.Category;
import com.ecommerce.entities.Product;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.mappers.EntityDtoMapper;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.services.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepo;
	private final CategoryRepository categoryRepo;
	private final EntityDtoMapper entityDtoMapper;
	private final AwsS3ServiceImpl awsS3Service;

	/**
	 * Creates a new product with the provided details.
	 *
	 * @param categoryId  The ID of the category to associate the product with.
	 * @param image       The product image file.
	 * @param name        The name of the product.
	 * @param description The description of the product.
	 * @param price       The price of the product.
	 * @return A Response indicating the result of the creation operation.
	 */
	@Override
	public Response createProduct(Long categoryId, MultipartFile image, String name, String description,
			BigDecimal price) {
		
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new NotFoundException("Category not found"));
		String productImageUrl = awsS3Service.saveImageToS3(image);

		Product product = new Product();
		product.setCategory(category);
		product.setPrice(price);
		product.setName(name);
		product.setDescription(description);
		product.setImageUrl(productImageUrl);

		productRepo.save(product);
		
		return Response.builder().status(200).message("Product successfully created").build();
	}

	/**
	 * Updates an existing product with the provided details.
	 *
	 * @param productId   The ID of the product to update.
	 * @param categoryId  The ID of the category to associate the product with
	 *                    (optional).
	 * @param image       The new product image file (optional).
	 * @param name        The new name of the product (optional).
	 * @param description The new description of the product (optional).
	 * @param price       The new price of the product (optional).
	 * @return A Response indicating the result of the update operation.
	 */
	@Override
	public Response updateProduct(Long productId, Long categoryId, MultipartFile image, String name, String description,
			BigDecimal price) {
		Product product = productRepo
				.findById(productId)
				.orElseThrow(() -> new NotFoundException("Product Not Found"));

		Category category = null;
		String productImageUrl = null;

		if (categoryId != null) {
			category = categoryRepo
					.findById(categoryId)
					.orElseThrow(() -> new NotFoundException("Category not found"));
		}
		if (image != null && !image.isEmpty()) {
			productImageUrl = awsS3Service.saveImageToS3(image);
		}

		if (category != null)
			product.setCategory(category);
		if (name != null)
			product.setName(name);
		if (price != null)
			product.setPrice(price);
		if (description != null)
			product.setDescription(description);
		if (productImageUrl != null)
			product.setImageUrl(productImageUrl);

		productRepo.save(product);
		
		return Response.builder().status(200).message("Product updated successfully").build();

	}

	/**
	 * Deletes a product by its ID.
	 *
	 * @param productId The ID of the product to delete.
	 * @return A Response indicating the result of the deletion operation.
	 */
	@Override
	public Response deleteProduct(Long productId) {
		
		Product product = productRepo
				.findById(productId)
				.orElseThrow(() -> new NotFoundException("Product Not Found"));
		
		productRepo.delete(product);

		return Response.builder().status(200).message("Product deleted successfully").build();
	}

	/**
	 * Retrieves a product by its ID.
	 *
	 * @param productId The ID of the product to retrieve.
	 * @return A Response containing the product details.
	 */
	@Override
	public Response getProductById(Long productId) {
		
		Product product = productRepo
				.findById(productId)
				.orElseThrow(() -> new NotFoundException("Product Not Found"));
		
		ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);

		return Response.builder().status(200).product(productDto).build();
	}

	/**
	 * Retrieves all products sorted by ID in descending order.
	 *
	 * @return A Response containing a list of all products.
	 */
	@Override
	public Response getAllProducts() {
		
		List<ProductDto> productList = productRepo
				.findAll(Sort.by(Sort.Direction.DESC, "id"))
				.stream()
				.map(entityDtoMapper::mapProductToDtoBasic).collect(Collectors.toList());

		return Response.builder().status(200).productList(productList).build();
	}

	/**
	 * Retrieves all products associated with a specific category.
	 *
	 * @param categoryId The ID of the category to filter products by.
	 * @return A Response containing a list of products in the specified category.
	 */
	@Override
	public Response getProductsByCategory(Long categoryId) {
		
		List<Product> products = productRepo.findByCategoryId(categoryId);
		if (products.isEmpty()) {
			throw new NotFoundException("No Products found for this category");
		}
		List<ProductDto> productDtoList = products
				.stream()
				.map(entityDtoMapper::mapProductToDtoBasic)
				.collect(Collectors.toList());

		return Response.builder().status(200).productList(productDtoList).build();
	}

	/**
	 * Searches for products based on a search value (name or description).
	 *
	 * @param searchValue The value to search for in product names or descriptions.
	 * @return A Response containing a list of matching products.
	 */
	@Override
	public Response searchProduct(String searchValue) {
		
		List<Product> products = productRepo
				.findByNameContainingOrDescriptionContaining(searchValue, searchValue);

		if (products.isEmpty()) {
			throw new NotFoundException("No Products Found");
		}
		List<ProductDto> productDtoList = products
				.stream()
				.map(entityDtoMapper::mapProductToDtoBasic)
				.collect(Collectors.toList());

		return Response.builder().status(200).productList(productDtoList).build();
	}

}
