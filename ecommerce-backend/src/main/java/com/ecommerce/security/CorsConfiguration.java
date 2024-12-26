package com.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

	/**
	 * Configures Cross-Origin Resource Sharing (CORS) settings for the application.
	 * 
	 * This bean defines a custom WebMvcConfigurer to allow CORS requests across the
	 * application. The configuration enables requests from any origin ("*") and
	 * supports the HTTP methods GET, POST, PUT, and DELETE.
	 * 
	 * Use cases: - Allows frontend applications hosted on different domains to
	 * communicate with this backend API. - Essential for enabling secure
	 * cross-origin interactions in distributed applications.
	 * 
	 * @return A WebMvcConfigurer instance with the specified CORS mappings.
	 */
	@Bean
	WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*");
			}
		};
	}
}
