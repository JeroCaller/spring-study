package com.jerocaller.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jerocaller.data.dto.response.RestResponse;
import com.jerocaller.exception.classes.BaseCustomException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(value = BaseCustomException.class)
    public ResponseEntity<RestResponse> CustomExceptionHandler(
        BaseCustomException e
    ) {
        return RestResponse.builder()
            .httpStatus(e.getHttpStatus())
            .message(e.getMessage())
            .build()
            .toResponse();
    }
    
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<RestResponse> usernameNotFoundExceptionHandler(
        UsernameNotFoundException e
    ) {
        return RestResponse.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .message(e.getMessage())
            .build()
            .toResponse();
    }
    
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse> notValidExceptionHandler(
        MethodArgumentNotValidException e
    ) {
        
        Map<String, String> notValidMsg = new HashMap<String, String>();
        
        e.getBindingResult().getFieldErrors().forEach(error -> {
            notValidMsg.put(error.getField(), error.getDefaultMessage());
        });
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .message("유효성 검사 실패.")
            .data(notValidMsg)
            .build()
            .toResponse();
    }
    
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<RestResponse> EntityNotFoundExceptionHandler(
        EntityNotFoundException e
    ) {
        return RestResponse.builder()
            .httpStatus(HttpStatus.NOT_FOUND)
            .message(e.getMessage())
            .build()
            .toResponse();
    }
    
}
