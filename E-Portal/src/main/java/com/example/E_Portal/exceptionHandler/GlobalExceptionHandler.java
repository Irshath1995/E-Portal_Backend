package com.example.e_portal.exceptionHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class) //this method will run whenever this MethodArgumentNotValidException is thrown.
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
		
		ex.getBindingResult().getFieldErrors().forEach(error -> {
	        errors.put(error.getField(), error.getDefaultMessage());
	    });
		
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DataIntegrityViolationException.class) //this method will run whenever this DataIntegrityViolationException is thrown.
	public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex){
		Map<String, String> error = new LinkedHashMap<>();
		error.put("error", "Database constraint violation");
		error.put("details", getRootCauseMessage(ex));
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	private String getRootCauseMessage(Throwable throwable) {
		Throwable root = throwable;
		while(root.getCause()!=null && root!=root.getCause()) {
			root = root.getCause();
		}
		
		return root.getMessage();
	}
	
	@ExceptionHandler(value = { IllegalArgumentException.class, RuntimeException.class })
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + ex.getMessage());
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + ex.getMessage());
    }
	

}
