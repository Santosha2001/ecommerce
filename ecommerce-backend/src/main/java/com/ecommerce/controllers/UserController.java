package com.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.Response;
import com.ecommerce.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
     * This endpoint handles the GET request to retrieve all users.
     * Only users with 'ADMIN' authority can access this method.
     * 
     * @return ResponseEntity<Response> A response entity containing the list of all users.
     */
	@GetMapping("/getAllUsers")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	/**
     * This endpoint handles the GET request to retrieve the logged-in user's information and order history.
     * 
     * @return ResponseEntity<Response> A response entity containing the logged-in user's information and order history.
     */
	@GetMapping("/loggedInUserInfo")
	public ResponseEntity<Response> getUserInfoAndOrderHistory() {
		return ResponseEntity.ok(userService.getUserInfoAndOrderHistory());
	}
}
