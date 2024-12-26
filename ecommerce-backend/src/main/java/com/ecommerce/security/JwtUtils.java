package com.ecommerce.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ecommerce.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtUtils is a utility class for managing JSON Web Tokens (JWT) in the
 * application. It handles token creation, validation, and extraction of claims
 * such as username.
 */
@Service
@Slf4j
public class JwtUtils {

	// Token expiration time set to 6 months
	private static final long EXPIRATION_TIME_IN_MILLISEC = 1000L * 60L * 60L * 24L * 30L * 6L; // expires 6 months

	// Secret key used for signing the JWT
	private SecretKey key;

	@Value("${secreteJwtString}")
	private String secreteJwtString; // Secret string (must be at least 32 characters)

	/**
	 * Initializes the secret key for signing JWT tokens using the configured secret
	 * string.
	 */
	@PostConstruct
	private void init() {
		byte[] keyBytes = secreteJwtString.getBytes(StandardCharsets.UTF_8);
		this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
	}

	/**
	 * Generates a JWT token for a given user.
	 *
	 * @param user The user for whom the token is being generated.
	 * @return A signed JWT token containing the user's email as the subject.
	 */
	public String generateToken(User user) {
		String username = user.getEmail();
		return generateToken(username);
	}

	/**
	 * Generates a JWT token for a given username.
	 *
	 * @param username The username to include in the token's subject.
	 * @return A signed JWT token.
	 */
	public String generateToken(String username) {
		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLISEC))
				.signWith(key)
				.compact();
	}

	/**
	 * Extracts the username (subject) from a JWT token.
	 *
	 * @param token The JWT token.
	 * @return The username extracted from the token.
	 */
	public String getUsernameFromToken(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	/**
	 * Extracts specific claims from a JWT token using a claims resolver function.
	 *
	 * @param token           The JWT token.
	 * @param claimsTFunction The function to extract specific claims.
	 * @param <T>             The type of the extracted claim.
	 * @return The extracted claim.
	 */
	private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
		return claimsTFunction.apply(
				Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload()
		);
	}

	/**
	 * Validates a JWT token against the user details.
	 *
	 * @param token       The JWT token.
	 * @param userDetails The user details to validate the token against.
	 * @return True if the token is valid and matches the user details, false
	 *         otherwise.
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	/**
	 * Checks if a JWT token has expired.
	 *
	 * @param token The JWT token.
	 * @return True if the token has expired, false otherwise.
	 */
	private boolean isTokenExpired(String token) {
		return extractClaims(token, Claims::getExpiration).before(new Date());
	}

}
