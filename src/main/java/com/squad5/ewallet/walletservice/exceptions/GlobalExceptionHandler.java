package com.squad5.ewallet.walletservice.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", ex.getMessage());
        response.put("timestamp", Instant.now().toString());
        response.put("type", ex.getClass().getSimpleName());

        logger.info("All Exception: ResponseSent - {}, Exception",response, ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle bad request / invalid params
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(MethodArgumentTypeMismatchException ex) {

        logger.info("Bad request: ", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "FAIL");
        response.put("message", "Invalid parameter: " + ex.getName());
        response.put("timestamp", Instant.now().toString());

        logger.info("All Exception: ResponseSent - {}, Exception",response, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex) {
        logger.error("404 Not Found: {}", ex.getRequestURL(), ex);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "FAIL");
        response.put("message", "No handler found for " + ex.getRequestURL());
        response.put("timestamp", Instant.now().toString());

        logger.info("404 Not Found: ResponseSent - {}, Exception",response, ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}


