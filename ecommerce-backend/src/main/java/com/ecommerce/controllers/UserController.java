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

	@GetMapping("/getAllUsers")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/loggedInUserInfo")
	public ResponseEntity<Response> getUserInfoAndOrderHistory() {
		return ResponseEntity.ok(userService.getUserInfoAndOrderHistory());
	}
}
