package com.eee.filmplatform.services.identity.common.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(DuplicateResourceException.class)
        public ResponseEntity<ErrorResponse> handleDuplicate(
                        DuplicateResourceException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(ErrorResponse.builder()
                                                .timestamp(Instant.now())
                                                .status(HttpStatus.CONFLICT.value())
                                                .error("Conflict")
                                                .message(ex.getMessage())
                                                .path(request.getRequestURI())
                                                .details(List.of())
                                                .build());
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(
                        ResourceNotFoundException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(ErrorResponse.builder()
                                                .timestamp(Instant.now())
                                                .status(HttpStatus.NOT_FOUND.value())
                                                .error("Not Found")
                                                .message(ex.getMessage())
                                                .path(request.getRequestURI())
                                                .details(List.of())
                                                .build());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .toList();

                return ResponseEntity.badRequest()
                                .body(ErrorResponse.builder()
                                                .timestamp(Instant.now())
                                                .status(400)
                                                .error("Validation Failed")
                                                .message("Input validation failed")
                                                .path(request.getRequestURI())
                                                .details(errors)
                                                .build());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {

                return ResponseEntity.badRequest()
                                .body(ErrorResponse.builder()
                                                .timestamp(Instant.now())
                                                .status(HttpStatus.BAD_REQUEST.value())
                                                .error("Bad Request")
                                                .message(ex.getMessage())
                                                .path(request.getRequestURI())
                                                .details(List.of())
                                                .build());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleException(
                        Exception ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ErrorResponse.builder()
                                                .timestamp(Instant.now())
                                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                .error("Internal Server Error")
                                                .message("An unexpected error occurred.")
                                                .path(request.getRequestURI())
                                                .details(List.of())
                                                .build());
        }

        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAuthorizationDenied(
                        AuthorizationDeniedException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(ErrorResponse.builder()
                                                .timestamp(Instant.now())
                                                .status(HttpStatus.FORBIDDEN.value())
                                                .error("Forbidden")
                                                .message("Access denied")
                                                .path(request.getRequestURI())
                                                .details(List.of())
                                                .build());
        }

        @ExceptionHandler(InvalidTokenException.class)
        public ResponseEntity<ErrorResponse> handleInvalidToken(
                        InvalidTokenException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(ErrorResponse.builder()
                                                .timestamp(Instant.now())
                                                .status(HttpStatus.UNAUTHORIZED.value())
                                                .error("Unauthorized")
                                                .message(ex.getMessage())
                                                .path(request.getRequestURI())
                                                .details(List.of())
                                                .build());
        }
}