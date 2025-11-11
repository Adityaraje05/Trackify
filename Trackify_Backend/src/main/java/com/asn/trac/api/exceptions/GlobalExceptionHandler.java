package com.asn.trac.api.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DivideByZeroException.class)
	public ResponseEntity<ErrorResponse> divideByZeroException(DivideByZeroException ex, HttpServletRequest request) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setStatusCode(500);
		errorResponse.setPath(request.getRequestURI());
		
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		StringBuilder errors = new StringBuilder("Validation failed: ");
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ");
		});
		return ResponseEntity.badRequest().body(errors.toString());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
		StringBuilder errors = new StringBuilder("Validation failed: ");
		ex.getConstraintViolations().forEach(violation -> {
			errors.append(violation.getPropertyPath()).append(" - ").append(violation.getMessage()).append("; ");
		});
		return ResponseEntity.badRequest().body(errors.toString());
	}

}
