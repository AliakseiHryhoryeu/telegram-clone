package com.example.telegramclone.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		StringBuilder errors = new StringBuilder();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			errors.append(error.getDefaultMessage()).append("\n");
		});
		return ResponseEntity.badRequest().body(errors.toString());
	}
}
