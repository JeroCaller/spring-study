package com.jerocaller.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jerocaller.dto.MembersResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestService {
    
    public Object doSomethingWithOnlyAuthed() {
        
        log.info("doSomethingWithOnlyAuthed 호출 시작.");
        
        Authentication auth = SecurityContextHolder
            .getContext()
            .getAuthentication();
        
        log.info("doSomethingWithOnlyAuthed 호출 끝.");
        
        return MembersResponse.builder()
            .nickname(auth.getName())
            .build();
    }

}
