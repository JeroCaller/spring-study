package com.jerocaller.exception.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        
        log.info("=== 인증 실패 ===");
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        String message = "인증되지 않았습니다. 로그인 하거나 액세스 토큰을 재발급하세요.";
        SecurityExceptionResponse securityExceptionResponse
            = SecurityExceptionResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .messasge(message)
                .build();
        
        response.setStatus(securityExceptionResponse.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(
            objectMapper.writeValueAsString(securityExceptionResponse)
        );
        
    }

}
