package com.ecommerce.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ecommerce.dto.AddressDto;
import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.OrderItemDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.UserDto;
import com.ecommerce.entities.Address;
import com.ecommerce.entities.Category;
import com.ecommerce.entities.OrderItem;
import com.ecommerce.entities.Product;
import com.ecommerce.entities.User;

@Component
public class EntityDtoMapper {

	/**
	 * Maps a User entity to a UserDto with basic fields.
	 * 
	 * @param user The User entity to map.
	 * @return The mapped UserDto.
	 */
	public UserDto mapUserToDtoBasic(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setPhoneNumber(user.getPhoneNumber());
		userDto.setEmail(user.getEmail());
		userDto.setRole(user.getRole().name());
		userDto.setName(user.getName());

		return userDto;
	}

	/**
	 * Maps an Address entity to an AddressDto with basic fields.
	 * 
	 * @param address The Address entity to map.
	 * @return The mapped AddressDto.
	 */
	public AddressDto mapAddressToDtoBasic(Address address) {
		AddressDto addressDto = new AddressDto();
		addressDto.setId(address.getId());
		addressDto.setCity(address.getCity());
		addressDto.setStreet(address.getStreet());
		addressDto.setState(address.getState());
		addressDto.setCountry(address.getCountry());
		addressDto.setZipCode(address.getZipCode());

		return addressDto;
	}

	/**
	 * Maps a Category entity to a CategoryDto with basic fields.
	 * 
	 * @param category The Category entity to map.
	 * @return The mapped CategoryDto.
	 */
	public CategoryDto mapCategoryToDtoBasic(Category category) {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(category.getId());
		categoryDto.setName(category.getName());

		return categoryDto;
	}

	/**
	 * Maps an OrderItem entity to an OrderItemDto with basic fields.
	 * 
	 * @param orderItem The OrderItem entity to map.
	 * @return The mapped OrderItemDto.
	 */
	public OrderItemDto mapOrderItemToDtoBasic(OrderItem orderItem) {
		OrderItemDto orderItemDto = new OrderItemDto();
		orderItemDto.setId(orderItem.getId());
		orderItemDto.setQuantity(orderItem.getQuantity());
		orderItemDto.setPrice(orderItem.getPrice());
		orderItemDto.setStatus(orderItem.getStatus().name());
		orderItemDto.setCreatedAt(orderItem.getCreatedAt());

		return orderItemDto;
	}

	/**
	 * Maps a Product entity to a ProductDto with basic fields.
	 * 
	 * @param product The Product entity to map.
	 * @return The mapped ProductDto.
	 */
	public ProductDto mapProductToDtoBasic(Product product) {
		ProductDto productDto = new ProductDto();
		productDto.setId(product.getId());
		productDto.setName(product.getName());
		productDto.setDescription(product.getDescription());
		productDto.setPrice(product.getPrice());
		productDto.setImageUrl(product.getImageUrl());

		return productDto;
	}

	/**
	 * Maps a User entity to a UserDto, including basic fields and address.
	 * 
	 * @param user The User entity to map.
	 * @return The mapped UserDto with address.
	 */
	public UserDto mapUserToDtoPlusAddress(User user) {

		UserDto userDto = mapUserToDtoBasic(user);
		if (user.getAddress() != null) {
			AddressDto addressDto = mapAddressToDtoBasic(user.getAddress());
			userDto.setAddress(addressDto);
		}

		return userDto;
	}

	/**
	 * Maps an OrderItem entity to an OrderItemDto, including basic fields and
	 * product details.
	 * 
	 * @param orderItem The OrderItem entity to map.
	 * @return The mapped OrderItemDto with product details.
	 */
	public OrderItemDto mapOrderItemToDtoPlusProduct(OrderItem orderItem) {
		OrderItemDto orderItemDto = mapOrderItemToDtoBasic(orderItem);
		if (orderItem.getProduct() != null) {
			ProductDto productDto = mapProductToDtoBasic(orderItem.getProduct());
			orderItemDto.setProduct(productDto);
		}

		return orderItemDto;
	}

	/**
	 * Maps an OrderItem entity to an OrderItemDto, including product and user
	 * details.
	 * 
	 * @param orderItem The OrderItem entity to map.
	 * @return The mapped OrderItemDto with product and user details.
	 */
	public OrderItemDto mapOrderItemToDtoPlusProductAndUser(OrderItem orderItem) {
		OrderItemDto orderItemDto = mapOrderItemToDtoPlusProduct(orderItem);
		if (orderItem.getUser() != null) {
			UserDto userDto = mapUserToDtoPlusAddress(orderItem.getUser());
			orderItemDto.setUser(userDto);
		}

		return orderItemDto;
	}

	/**
	 * Maps a User entity to a UserDto, including address and order item history.
	 * 
	 * @param user The User entity to map.
	 * @return The mapped UserDto with address and order history.
	 */
	public UserDto mapUserToDtoPlusAddressAndOrderHistory(User user) {
		UserDto userDto = mapUserToDtoPlusAddress(user);
		if (user.getOrderItemList() != null && !user.getOrderItemList().isEmpty()) {
			userDto.setOrderItemList(user.getOrderItemList().stream()
					.map(this::mapOrderItemToDtoPlusProduct)
					.collect(Collectors.toList()));
		}

		return userDto;
	}

}
