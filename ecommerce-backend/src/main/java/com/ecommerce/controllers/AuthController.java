package com.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.Response;
import com.ecommerce.dto.UserDto;
import com.ecommerce.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<Response> registerUser(@RequestBody UserDto registrationRequest) {
		System.out.println(registrationRequest);
		return ResponseEntity.ok(userService.registerUser(registrationRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<Response> loginUser(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(userService.loginUser(loginRequest));
	}
}