package com.ecommerce.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.Response;
import com.ecommerce.enums.OrderStatus;

public interface OrderItemService {

	Response placeOrder(OrderRequest orderRequest);

	Response updateOrderItemStatus(Long orderItemId, String status);

	Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId,
			Pageable pageable);
}
