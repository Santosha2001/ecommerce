package com.ecommerce.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.entities.User;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	/**
	 * Loads user details based on the provided username (email) for authentication.
	 * 
	 * This method queries the database for a user with the given email and maps the
	 * user entity to a Spring Security compatible `UserDetails` object.
	 * 
	 * @param username The email of the user trying to authenticate.
	 * @return A `UserDetails` object for authentication.
	 * @throws UsernameNotFoundException If no user is found with the given email.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new NotFoundException("User not found with Email: " + username));

		return AuthUser.builder().user(user).build();
	}

}
