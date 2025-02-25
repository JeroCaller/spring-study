package com.jerocaller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jerocaller.data.dto.response.RestResponse;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class JwtExceptionHandler {
    
    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<RestResponse> handleJwtExceptions(JwtException e) {
        return RestResponse.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .message(e.getMessage())
            .build()
            .toResponse();
    }
    
}
