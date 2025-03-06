package com.jerocaller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.business.TokenService;
import com.jerocaller.data.dto.response.MemberResponse;
import com.jerocaller.data.dto.response.RestResponse;

import lombok.RequiredArgsConstructor;

/**
 * OAuth2를 이용한 인증 관련 작업을 다루는 컨트롤러.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class OAuth2Controller {
    
    private final TokenService tokenService;
    
    @GetMapping("/login/success")
    public ResponseEntity<RestResponse> loginSuccess(
        @RequestParam("token") String accessToken
    ) {
        
        MemberResponse memberResponse = tokenService
            .getMemberInfoFromAccessToken(accessToken);
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("OAuth2 인증 성공")
            .data(memberResponse)
            .build()
            .toResponse();
    }
    
}
