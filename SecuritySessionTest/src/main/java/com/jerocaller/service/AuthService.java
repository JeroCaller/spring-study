package com.jerocaller.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.jerocaller.dto.MembersRequest;
import com.jerocaller.dto.MembersResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final AuthenticationManager authManager;
    
    /**
     * 인증 후 인증이 필요한 다른 API 호출 시 미인증 상태라고 뜸. 
     * 
     * @param request
     * @return
     */
    public MembersResponse login(MembersRequest request) {
        
        UsernamePasswordAuthenticationToken token 
            = new UsernamePasswordAuthenticationToken(
                request.getNickname(),
                request.getPassword()
            );
        
        Authentication authentication = authManager
            .authenticate(token);
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        
        log.info("인증된 사용자 정보: {}", SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName()
        );
        
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        
        return MembersResponse.builder()
            .nickname(loggedInUser.getUsername())
            .build();
    }
}
