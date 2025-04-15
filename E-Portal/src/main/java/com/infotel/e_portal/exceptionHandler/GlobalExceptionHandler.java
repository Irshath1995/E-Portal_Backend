package com.infotel.e_portal.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Handle specific exceptions
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
		return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<Object> handleInvalidData(InvalidDataException ex) {
		return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	// Handle all exceptions as a fallback
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex) {
		return new ResponseEntity<>(new ErrorResponse("INTERNAL_SERVER_ERROR", "Something went wrong!"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Handle validation exceptions (for example, BindException)
	@ExceptionHandler(BindException.class)
	public ResponseEntity<Object> handleValidationException(BindException ex) {
		return new ResponseEntity<>(new ErrorResponse("VALIDATION_FAILED", "Validation failed for the input"), HttpStatus.BAD_REQUEST);
	}
}
