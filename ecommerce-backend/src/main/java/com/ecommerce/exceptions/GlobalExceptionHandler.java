package com.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ecommerce.dto.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles all uncaught exceptions (generic exceptions) that are not explicitly
	 * handled by other methods. Returns a 500 Internal Server Error response.
	 *
	 * @param exception The exception that occurred.
	 * @param request   The WebRequest object providing details about the request.
	 * @return A ResponseEntity containing the error details with HTTP status 500.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleAllExceptions(Exception exception, WebRequest request) {
		Response errorResponse = Response.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(exception.getMessage())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles NotFoundException, which is thrown when a requested resource is not
	 * found. Returns a 404 Not Found response.
	 *
	 * @param exception The NotFoundException that occurred.
	 * @param request   The WebRequest object providing details about the request.
	 * @return A ResponseEntity containing the error details with HTTP status 404.
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Response> handleNotFoundException(NotFoundException exception, WebRequest request) {
		Response errorResponse = Response.builder()
				.status(HttpStatus.NOT_FOUND.value())
				.message(exception.getMessage())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles InvalidCredentialsException, which is thrown when user credentials
	 * are invalid. Returns a 400 Bad Request response.
	 *
	 * @param exception The InvalidCredentialsException that occurred.
	 * @param request   The WebRequest object providing details about the request.
	 * @return A ResponseEntity containing the error details with HTTP status 400.
	 */
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Response> handleInvalidCredentialsException(InvalidCredentialsException exception,
			WebRequest request) {
		Response errorResponse = Response.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message(exception.getMessage())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
