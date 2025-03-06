package com.jerocaller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.business.MemberService;
import com.jerocaller.data.dto.request.MemberRequest;
import com.jerocaller.data.dto.response.MemberResponse;
import com.jerocaller.data.dto.response.RestResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    
    private final MemberService memberService;
    
    @PostMapping("/registration")
    public ResponseEntity<RestResponse> register( 
        @Valid @RequestBody MemberRequest memberRequest
    ) {
        
        MemberResponse memberResponse = memberService.register(memberRequest);
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.CREATED)
            .message("회원 가입 성공")
            .data(memberResponse)
            .build()
            .toResponse();
    }
    
}
