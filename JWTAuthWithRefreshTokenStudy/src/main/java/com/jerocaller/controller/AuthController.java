package com.jerocaller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.business.AuthService;
import com.jerocaller.common.CookieNames;
import com.jerocaller.common.ExpirationTime;
import com.jerocaller.data.dto.request.JwtCookieRequest;
import com.jerocaller.data.dto.request.MemberRequest;
import com.jerocaller.data.dto.response.MemberResponse;
import com.jerocaller.data.dto.response.RestResponse;
import com.jerocaller.jwt.JwtCookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;
    private final JwtCookieUtil jwtCookieUtil;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    
    @PostMapping("/login")
    public ResponseEntity<RestResponse> login(
        @Valid @RequestBody MemberRequest memberRequest
    ) {
        
        MemberResponse memberResponse = authService.login(memberRequest);
        
        // 로그인 성공 시 클라이언트에 JWT 토큰이 담긴 쿠키 전송.
        jwtCookieUtil.addCookieWithJwt(
            httpServletResponse,
            JwtCookieRequest.builder()
                .cookieName(CookieNames.ACCESS_TOKEN)
                .token(memberResponse.getTokens().getAccessToken())
                .maxAge(ExpirationTime.ACCESS_TOKEN_IN_SECONDS)
                .build()
        );
        jwtCookieUtil.addCookieWithJwt(
            httpServletResponse, 
            JwtCookieRequest.builder()
                .cookieName(CookieNames.REFRESH_TOKEN)
                .token(memberResponse.getTokens().getRefreshToken())
                .maxAge(ExpirationTime.REFRESH_TOKEN_IN_SECONDS)
                .build()
        );
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("로그인 성공")
            .data(memberResponse)
            .build()
            .toResponse();
    }
    
    @PostMapping("/logout")
    public ResponseEntity<RestResponse> logout() {
        
        authService.logout(httpServletRequest, httpServletResponse);
        
        jwtCookieUtil.deleteCookies(
            httpServletResponse, 
            CookieNames.ACCESS_TOKEN, 
            CookieNames.REFRESH_TOKEN
        );
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("로그아웃 성공")
            .build()
            .toResponse();
    }
    
}
