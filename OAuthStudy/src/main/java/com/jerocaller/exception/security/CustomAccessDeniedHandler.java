package com.jerocaller.exception.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 인가(Authorization) 과정에서 예외 발생 시 이를 처리할 핸들러.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
        HttpServletRequest request, 
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        SecurityExceptionResponse securityExceptionResponse
            = SecurityExceptionResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .messasge("접근 권한 없음.")
                .build();
        
        response.setStatus(securityExceptionResponse.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(
            objectMapper.writeValueAsString(securityExceptionResponse)
        );
        
    }
    
}
