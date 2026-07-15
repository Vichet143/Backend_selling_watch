package com.example.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.headers.HeadersSecurityMarker;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandle{

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<?> handleServiceException(ApiException se){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, se.getSuccess(), se.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
