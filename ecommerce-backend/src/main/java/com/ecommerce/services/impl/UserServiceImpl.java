package com.ecommerce.services.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.Response;
import com.ecommerce.dto.UserDto;
import com.ecommerce.entities.User;
import com.ecommerce.enums.UserRole;
import com.ecommerce.exceptions.InvalidCredentialsException;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.mappers.EntityDtoMapper;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.security.JwtUtils;
import com.ecommerce.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;
	private final EntityDtoMapper entityDtoMapper;

	/**
	 * Registers a new user with the provided details. Assigns a default role of
	 * "USER" unless specified as "ADMIN".
	 *
	 * @param registrationRequest The user registration details.
	 * @return A Response containing the status and details of the registered user.
	 */
	@Override
	public Response registerUser(UserDto registrationRequest) {
		
		UserRole role = UserRole.USER;
		if (registrationRequest.getRole() != null && 
				registrationRequest.getRole().equalsIgnoreCase("admin")) {
			role = UserRole.ADMIN;
		}

		User user = User
				.builder()
				.name(registrationRequest.getName())
				.email(registrationRequest.getEmail())
				.password(passwordEncoder.encode(registrationRequest.getPassword()))
				.phoneNumber(registrationRequest.getPhoneNumber())
				.role(role)
				.build();

		User savedUser = userRepo.save(user);

		UserDto userDto = entityDtoMapper.mapUserToDtoBasic(savedUser);
		
		return Response
				.builder()
				.status(200)
				.message("User Successfully Added")
				.user(userDto)
				.build();
	}

	/**
	 * Authenticates a user by validating their email and password. Generates a JWT
	 * token for authenticated users.
	 *
	 * @param loginRequest The login credentials (email and password).
	 * @return A Response containing the authentication status and a JWT token.
	 */
	@Override
	public Response loginUser(LoginRequest loginRequest) {

		User user = userRepo.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new NotFoundException("Email not found"));
		
		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Password does not match");
		}
		String token = jwtUtils.generateToken(user);

		return Response
				.builder()
				.status(200)
				.message("User Successfully Logged In")
				.token(token)
				.expirationTime("6 Month")
				.role(user.getRole().name())
				.build();
	}

	/**
	 * Fetches all registered users in the system.
	 *
	 * @return A Response containing a list of all users.
	 */
	@Override
	public Response getAllUsers() {

		List<User> users = userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(entityDtoMapper::mapUserToDtoBasic).toList();

		return Response.builder().status(200).userList(userDtos).build();
	}

	/**
	 * Retrieves the currently logged-in user's details from the security context.
	 *
	 * @return The User entity representing the logged-in user.
	 */
	@Override
	public User getLoginUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String email = authentication.getName();
		log.info("User Email is: " + email);
		
		return userRepo
				.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User Not found"));
	}

	/**
	 * Retrieves the logged-in user's details along with their order history.
	 *
	 * @return A Response containing the user's details and order history.
	 */
	@Override
	public Response getUserInfoAndOrderHistory() {
		
		User user = getLoginUser();
		UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);

		return Response.builder().status(200).user(userDto).build();
	}
}
