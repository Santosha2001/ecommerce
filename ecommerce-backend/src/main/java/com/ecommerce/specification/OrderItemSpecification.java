package com.ecommerce.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.entities.OrderItem;
import com.ecommerce.enums.OrderStatus;

public class OrderItemSpecification {

	/**
	 * Creates a specification to filter order items by their status.
	 *
	 * @param status The desired OrderStatus to filter by.
	 * @return A Specification for filtering OrderItem entities by status.
	 */
	public static Specification<OrderItem> hasStatus(OrderStatus status) {
		
		return ((root, query, criteriaBuilder) -> 
			status != null ? criteriaBuilder.equal(root.get("status"), status) : null);
	}

	/**
	 * Creates a specification to filter order items based on their creation date
	 * range.
	 *
	 * @param startDate The start of the date range.
	 * @param endDate   The end of the date range.
	 * @return A Specification for filtering OrderItem entities by creation date.
	 */
	public static Specification<OrderItem> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return ((root, query, criteriaBuilder) -> {
			
			if (startDate != null && endDate != null) {
				return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
			} else if (startDate != null) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
			} else if (endDate != null) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
			} else {
				return null;
			}
		});
	}

	/**
	 * Creates a specification to filter order items by their unique ID.
	 *
	 * @param itemId The ID of the OrderItem to filter by.
	 * @return A Specification for filtering OrderItem entities by ID.
	 */
	public static Specification<OrderItem> hasItemId(Long itemId) {
		return ((root, query, criteriaBuilder) -> 
			itemId != null ? criteriaBuilder.equal(root.get("id"), itemId) : null);
	}
}
