package com.softcafe.restaurant_system.configs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class ErrorConfig {
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, String>> responseStatusHandler(
      ResponseStatusException exception) {
    String message = exception.getReason() == null ? exception.getMessage() : exception.getReason();
    log.error(message);
    return ResponseEntity.status(exception.getStatusCode()).body(Map.of("message", message));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> messageNotReadableHandler(
      HttpMessageNotReadableException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("message", "Invalid request body!"));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> invalidArgumentHandler(
      MethodArgumentNotValidException exception) {
    List<String> errors = exception.getAllErrors().stream().map(error -> {
      return error.getDefaultMessage();
    }).toList();

    Map<String, Object> response = new HashMap<>();

    response.put("message", "An error has been encountered while processing your request!");
    response.put("errors", errors);

    log.warn("An error has occurred while processing the request: {}", errors);

    return ResponseEntity.status(400).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> exceptionHandler(Exception e) {
    log.error(e.getMessage());
    return ResponseEntity.status(500).body(Map.of("message", "Internal server error!"));
  }
}
