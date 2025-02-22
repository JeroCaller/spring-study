package com.jerocaller.exception.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

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
        // 응답 메시지 불필요시 다음과 같이 간단하게 status code만 
        // 정해 응답할 수도 있음. 
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        
    }

}
