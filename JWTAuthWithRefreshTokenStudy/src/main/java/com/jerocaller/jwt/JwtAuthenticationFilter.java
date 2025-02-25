package com.jerocaller.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jerocaller.common.RoleNames;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        String accessToken = jwtTokenProvider.resolveToken(request);
        
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        // For Test
        logUser(accessToken);
        
        filterChain.doFilter(request, response);
    }
    
    private void logUser(String token) {
        
        Authentication auth = SecurityContextHolder
            .getContext()
            .getAuthentication();
        
        if (auth == null) {
            log.info("=== 현재 인증된 정보 없음 ===");
            return;
        }
        
        String role = auth.getAuthorities().toArray()[0].toString();
        
        log.info("=== 현재 인증 정보 ===");
        log.info("id: {}", auth.getName());
        log.info("role: {}", auth.getAuthorities().toString());
        log.info("role String: {}", role);
        log.info(
            "role match: {}", 
            role.contains(RoleNames.USER)
        );
        log.info("token: {}", token);
        
    }

}
