package com.ansbeno.films.shared.exception;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles exceptions for REST API controllers ({@code @RestController}) only.
 * Produces JSON responses. MVC/Thymeleaf exceptions are handled by
 * {@link GlobalExceptionHandler}.
 */
@RestControllerAdvice(annotations = RestController.class)
public class RestApiExceptionHandler {

      @ExceptionHandler(MethodArgumentNotValidException.class)
      public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
            Map<String, List<String>> fieldErrors = new LinkedHashMap<>();
            ex.getBindingResult().getFieldErrors()
                        .forEach(fe -> fieldErrors
                                    .computeIfAbsent(fe.getField(), k -> new ArrayList<>())
                                    .add(fe.getDefaultMessage()));
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
            body.put("error", "Validation Failed");
            body.put("fieldErrors", fieldErrors);
            return ResponseEntity.unprocessableEntity()
                        .body(body);
      }

      @ExceptionHandler(UserAlreadyExistsException.class)
      public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", ex.getMessage()));
      }

      @ExceptionHandler(ResourceNotFoundException.class)
      public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", ex.getMessage()));
      }

      @ExceptionHandler(BookingCapacityExceededException.class)
      public ResponseEntity<Map<String, String>> handleBookingCapacityExceeded(BookingCapacityExceededException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", ex.getMessage()));
      }

      @ExceptionHandler(BookingOperationNotAllowedException.class)
      public ResponseEntity<Map<String, String>> handleBookingOperationNotAllowed(
                  BookingOperationNotAllowedException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", ex.getMessage()));
      }

      @ExceptionHandler(BookingAccessDeniedException.class)
      public ResponseEntity<Map<String, String>> handleBookingAccessDenied(BookingAccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", ex.getMessage()));
      }

}
