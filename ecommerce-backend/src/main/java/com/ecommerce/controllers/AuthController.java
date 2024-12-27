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

	/**
	 * This endpoint handles the POST request to register a new user.
	 * 
	 * @param registrationRequest The UserDto object containing the user details for
	 *                            registration.
	 * @return ResponseEntity<Response> A response entity containing the result of
	 *         the registration operation.
	 */
	@PostMapping("/register")
	public ResponseEntity<Response> registerUser(@RequestBody UserDto registrationRequest) {
		
		return ResponseEntity.ok(userService.registerUser(registrationRequest));
	}

	/**
	 * This endpoint handles the POST request for user login.
	 * 
	 * @param loginRequest The LoginRequest object containing the user's login
	 *                     credentials.
	 * @return ResponseEntity<Response> A response entity containing the result of
	 *         the login operation.
	 */
	@PostMapping("/login")
	public ResponseEntity<Response> loginUser(@RequestBody LoginRequest loginRequest) {
		
		return ResponseEntity.ok(userService.loginUser(loginRequest));
	}
}
