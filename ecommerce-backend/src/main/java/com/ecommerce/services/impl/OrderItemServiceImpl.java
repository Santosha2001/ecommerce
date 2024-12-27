package com.ecommerce.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.OrderItemDto;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.Response;
import com.ecommerce.entities.Order;
import com.ecommerce.entities.OrderItem;
import com.ecommerce.entities.Product;
import com.ecommerce.entities.User;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.mappers.EntityDtoMapper;
import com.ecommerce.repositories.OrderItemRepository;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.services.OrderItemService;
import com.ecommerce.services.UserService;
import com.ecommerce.specification.OrderItemSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final ProductRepository productRepo;
	private final UserService userService;
	private final EntityDtoMapper entityDtoMapper;

	/**
	 * Places an order for the logged-in user based on the provided order request.
	 *
	 * @param orderRequest The details of the order to be placed.
	 * @return A Response indicating the status of the operation.
	 */
	@Override
	public Response placeOrder(OrderRequest orderRequest) {

		User user = userService.getLoginUser();

		List<OrderItem> orderItems = orderRequest.getItems().stream().map(orderItemRequest -> {

			Product product = productRepo.findById(orderItemRequest.getProductId())
					.orElseThrow(() -> new NotFoundException("Product Not Found"));

			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(product);
			orderItem.setQuantity(orderItemRequest.getQuantity());
			orderItem.setPrice(product
								.getPrice()
								.multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())));

			orderItem.setStatus(OrderStatus.PENDING);
			orderItem.setUser(user);

			return orderItem;

		}).collect(Collectors.toList());

		// calculate the total price
		BigDecimal totalPrice = orderRequest.getTotalPrice() != null
				&& orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) > 0 ? 
						orderRequest.getTotalPrice() : 
							orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

		// create order entity
		Order order = new Order();
		order.setOrderItemList(orderItems);
		order.setTotalPrice(totalPrice);

		// set the order reference in each order item
		orderItems.forEach(orderItem -> orderItem.setOrder(order));

		orderRepo.save(order);

		return Response.builder().status(200).message("Order was successfully placed").build();
	}

	/**
	 * Updates the status of an order item by its ID.
	 *
	 * @param orderItemId The ID of the order item to update.
	 * @param status      The new status to be assigned.
	 * @return A Response indicating the status of the operation.
	 */
	@Override
	public Response updateOrderItemStatus(Long orderItemId, String status) {
		
		OrderItem orderItem = orderItemRepo.findById(orderItemId)
				.orElseThrow(() -> new NotFoundException("Order Item not found"));

		orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
		orderItemRepo.save(orderItem);
		
		return Response
				.builder()
				.status(200)
				.message("Order status updated successfully")
				.build();
	}

	/**
	 * Filters order items based on the provided criteria.
	 *
	 * @param status    The order status to filter by.
	 * @param startDate The start date for the filter.
	 * @param endDate   The end date for the filter.
	 * @param itemId    The ID of the order item to filter by.
	 * @param pageable  The pagination details.
	 * @return A Response containing the filtered order items.
	 */
	@Override
	public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId,
			Pageable pageable) {
		
		Specification<OrderItem> spec = Specification
				.where(OrderItemSpecification.hasStatus(status))
				.and(OrderItemSpecification.createdBetween(startDate, endDate))
				.and(OrderItemSpecification.hasItemId(itemId));

		Page<OrderItem> orderItemPage = orderItemRepo.findAll(spec, pageable);

		if (orderItemPage.isEmpty()) {
			throw new NotFoundException("No Order Found");
		}
		
		List<OrderItemDto> orderItemDtos = orderItemPage.getContent().stream()
				.map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
				.collect(Collectors.toList());

		return Response
				.builder()
				.status(200)
				.orderItemList(orderItemDtos)
				.totalPage(orderItemPage.getTotalPages())
				.totalElement(orderItemPage.getTotalElements())
				.build();
	}
}
