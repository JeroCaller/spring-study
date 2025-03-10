package com.jerocaller.config.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 구글 OAuth 로그인에 대해 CORS 에러가 발생하여 이를 바로잡기 위한 로깅 필터. 
 * 현재는 해당 문제 해결함.
 */
@Slf4j
public class CorsLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        log.info("===== CORS 필터 logging 시작 =====");
        
        String originOnRequest = request.getHeader("Origin");
        log.info("CORS Request Origin: {}", originOnRequest);
        
        filterChain.doFilter(request, response);
        
        String accessControlAllowOrigin = response
            .getHeader("Access-Control-Allow-Origin");
        log.info(
            "CORS Response Headers: {}", 
            accessControlAllowOrigin
        );
        
        if (originOnRequest != null && accessControlAllowOrigin != null) {
            log.info(
                "CORS Are their Origin Same? : {}", 
                originOnRequest.equals(accessControlAllowOrigin)
            );
        }
        
        log.info("===== CORS 필터 logging 끝 =====");
        
    }
    
}
