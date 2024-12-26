package com.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	// Custom filter for processing JWT authentication
	private final JwtAuthFilter jwtAuthFilter;

	/**
	 * Configures the security filter chain for HTTP requests.
	 * 
	 * @param httpSecurity The HttpSecurity instance for configuring security.
	 * @return The configured SecurityFilterChain.
	 * @throws Exception if an error occurs while building the security
	 *                   configuration.
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			// Disable CSRF and enable default CORS configuration
			.csrf(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			// Configure request authorization rules
			.authorizeHttpRequests(
					request -> request.requestMatchers("/auth/**", "/category/**", "/product/**", "/order/**")
							.permitAll()
							.anyRequest()
							.authenticated())
			// Set session management to stateless for JWT-based authentication
			.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// Add the custom JWT filter before the default authentication filter
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}

	/**
	 * Provides a password encoder for securely hashing passwords using BCrypt.
	 * 
	 * @return A PasswordEncoder instance.
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures the authentication manager to use the application's authentication
	 * settings.
	 * 
	 * @param authenticationConfiguration The authentication configuration.
	 * @return The configured AuthenticationManager.
	 * @throws Exception if an error occurs while obtaining the
	 *                   AuthenticationManager.
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
