package com.jerocaller.config.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    
    @Value("${frontend.url.oauth2.login.failure}")
    private String REDIRECT_URI;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException, ServletException {
        
        log.error("=== OAuth2 로그인 중 에러 발생 ===");
        log.error(exception.getMessage());
        response.sendRedirect(REDIRECT_URI);
        
    }
    
}
