package com.shoppie.globals;

import com.shoppie.exceptions.*;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return new ResponseEntity<>(ApiResponse.error("Constraint violated", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(ApiResponse.error("Validation failed", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpCooldownException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(OtpCooldownException exception) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleJsonParseException(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(
                ApiResponse.error(exception.getMostSpecificCause().getMessage()), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException exception) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException exception
    ) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncompatibleStatusChangeException.class)
    public ResponseEntity<ApiResponse<Void>> handleIncompatibleStatusChangeException(
            IncompatibleStatusChangeException exception
    ) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncompatiblePasswordException.class)
    public ResponseEntity<ApiResponse<Void>> handleIncompatiblePasswordException(
            IncompatiblePasswordException exception
    ) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception exception) {
        return new ResponseEntity<>(
                ApiResponse.error("An unexpected error occurred", exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
