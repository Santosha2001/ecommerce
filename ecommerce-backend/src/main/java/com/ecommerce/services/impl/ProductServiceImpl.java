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

	@Override
	public Response updateProduct(Long productId, Long categoryId, MultipartFile image, String name, String description,
			BigDecimal price) {
		Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));

		Category category = null;
		String productImageUrl = null;

		if (categoryId != null) {
			category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
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

	@Override
	public Response deleteProduct(Long productId) {
		Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
		productRepo.delete(product);

		return Response.builder().status(200).message("Product deleted successfully").build();
	}

	@Override
	public Response getProductById(Long productId) {
		Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
		ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);

		return Response.builder().status(200).product(productDto).build();
	}

	@Override
	public Response getAllProducts() {
		List<ProductDto> productList = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
				.map(entityDtoMapper::mapProductToDtoBasic).collect(Collectors.toList());

		return Response.builder().status(200).productList(productList).build();

	}

	@Override
	public Response getProductsByCategory(Long categoryId) {
		List<Product> products = productRepo.findByCategoryId(categoryId);
		if (products.isEmpty()) {
			throw new NotFoundException("No Products found for this category");
		}
		List<ProductDto> productDtoList = products.stream().map(entityDtoMapper::mapProductToDtoBasic)
				.collect(Collectors.toList());

		return Response.builder().status(200).productList(productDtoList).build();

	}

	@Override
	public Response searchProduct(String searchValue) {
		List<Product> products = productRepo.findByNameContainingOrDescriptionContaining(searchValue, searchValue);

		if (products.isEmpty()) {
			throw new NotFoundException("No Products Found");
		}
		List<ProductDto> productDtoList = products.stream().map(entityDtoMapper::mapProductToDtoBasic)
				.collect(Collectors.toList());

		return Response.builder().status(200).productList(productDtoList).build();
	}

}
