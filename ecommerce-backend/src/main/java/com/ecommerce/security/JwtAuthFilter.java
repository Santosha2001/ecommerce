package com.ecommerce.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final CustomUserDetailsService customUserDetailsService;
	private final JwtUtils jwtUtils;

	/**
	 * Filters incoming requests to validate the JWT token in the Authorization
	 * header. If the token is valid, the user is authenticated and their details
	 * are added to the Spring Security context.
	 *
	 * @param request     The HTTP request object.
	 * @param response    The HTTP response object.
	 * @param filterChain The filter chain to continue processing the request.
	 * @throws ServletException In case of servlet-related errors.
	 * @throws IOException      In case of I/O errors during request processing.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Extract the JWT token from the Authorization header
		String token = getTokenFromRequest(request);

		if (token != null) {

			// Extract the username from the token
			String username = jwtUtils.getUsernameFromToken(token);

			// Load user details using the username
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

			// Validate the token and authenticate the user
			if (StringUtils.hasText(username) && jwtUtils.isTokenValid(token, userDetails)) {
				log.info("VALID JWT FOR {}", username);

				// Create an authentication token and set it in the security context
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}

		}
		// Continue with the filter chain
		filterChain.doFilter(request, response);
	}

	/**
	 * Extracts the JWT token from the Authorization header of the HTTP request.
	 *
	 * @param request The HTTP request object.
	 * @return The JWT token if present and starts with "Bearer ", otherwise null.
	 */
	private String getTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (StringUtils.hasText(token) && StringUtils.startsWithIgnoreCase(token, "Bearer ")) {
			return token.substring(7); // Remove "Bearer " prefix
		}
		return null;
	}

}
