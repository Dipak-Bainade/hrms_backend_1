package com.hrms.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hrms.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)

                .body(

                        ErrorResponse.builder()

                                .success(false)

                                .message(ex.getMessage())

                                .build()

                );

    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(
            DuplicateResourceException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)

                .body(

                        ErrorResponse.builder()

                                .success(false)

                                .message(ex.getMessage())

                                .build()

                );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError :
                ex.getBindingResult().getFieldErrors()) {

            errors.put(

                    fieldError.getField(),

                    fieldError.getDefaultMessage()

            );

        }

        return ResponseEntity.badRequest()

                .body(

                        ErrorResponse.builder()

                                .success(false)

                                .message("Validation failed.")

                                .errors(errors)

                                .build()

                );

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> general(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

                .body(

                        ErrorResponse.builder()

                                .success(false)

                                .message(ex.getMessage())

                                .build()

                );

    }

	

}
