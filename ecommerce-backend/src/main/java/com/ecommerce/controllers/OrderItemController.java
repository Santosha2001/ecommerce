package com.ecommerce.controllers;

import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.Response;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.services.OrderItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderItemController {

	private final OrderItemService orderItemService;

	/**
     * This endpoint handles the POST request to place a new order.
     * 
     * @param orderRequest The OrderRequest object containing the details of the order to be placed.
     * @return ResponseEntity<Response> A response entity containing the result of the order placement operation.
     */
	@PostMapping("/create")
	public ResponseEntity<Response> placeOrder(@RequestBody OrderRequest orderRequest) {
		
		return ResponseEntity.ok(orderItemService.placeOrder(orderRequest));
	}

	/**
     * This endpoint handles the PUT request to update the status of a specific order item.
     * Only users with 'ADMIN' authority can access this method.
     * 
     * @param orderItemId The ID of the order item whose status is to be updated.
     * @param status The new status to be set for the order item.
     * @return ResponseEntity<Response> A response entity containing the result of the status update operation.
     */
	@PutMapping("/updateItemStatus/{orderItemId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> updateOrderItemStatus(@PathVariable Long orderItemId, @RequestParam String status) {
		
		return ResponseEntity.ok(orderItemService.updateOrderItemStatus(orderItemId, status));
	}

	/**
     * This endpoint handles the GET request to filter order items based on provided criteria.
     * Only users with 'ADMIN' authority can access this method.
     * 
     * @param startDate The start date for filtering orders (optional).
     * @param endDate The end date for filtering orders (optional).
     * @param status The status of the order items to filter by (optional).
     * @param itemId The ID of the order item to filter by (optional).
     * @param page The page number for pagination (default: 0).
     * @param size The page size for pagination (default: 1000).
     * @return ResponseEntity<Response> A response entity containing the filtered list of order items.
     */
	@GetMapping("/filter")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> filterOrderItems(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
			@RequestParam(required = false) String status, @RequestParam(required = false) Long itemId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1000") int size

	) {
		// Creates a Pageable object for pagination with sorting by ID in descending order.
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		OrderStatus orderStatus = status != null ? OrderStatus.valueOf(status.toUpperCase()) : null;

		return ResponseEntity.ok(orderItemService.filterOrderItems(orderStatus, startDate, endDate, itemId, pageable));

	}
}
