package com.example.backend.presentation.rest.v1.controller.advice;

import com.example.backend.application.excepiton.EntityNotFoundException;
import com.example.backend.presentation.rest.v1.dto.out.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFund(EntityNotFoundException ex, HttpServletRequest request) {
        log.error("Entity not found in the request [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);

        String message = String.format("%s not found", ex.getEntityName());

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .message(message)
                .errors(Collections.singletonList(ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        log.error("Validation error in the request [{}]: {}", request.getRequestURI(), ex.getMessage());

        List<String> errors = new ArrayList<>();

        List<String> fieldErrors  = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .toList();
        errors.addAll(fieldErrors);

        List<String> globalErrors = ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        errors.addAll(globalErrors);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .message("Validation error")
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        log.error("Constraint violation in the request [{}]: {}", request.getRequestURI(), ex.getMessage());

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                .collect(Collectors.toList());

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .message("Validation error")
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Generic error in the request [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .message("Internal Server Error")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
