package com.jerocaller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.business.TokenService;
import com.jerocaller.common.CookieNames;
import com.jerocaller.common.ExpirationTime;
import com.jerocaller.data.dto.request.CookieRequest;
import com.jerocaller.data.dto.request.TokenRequest;
import com.jerocaller.data.dto.response.RestResponse;
import com.jerocaller.data.dto.response.TokenResponse;
import com.jerocaller.util.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt/tokens")
public class TokenController {
    
    private final TokenService tokenService;
    private final HttpServletResponse httpServletResponse;
    private final CookieUtil jwtCookieUtil;
    
    /**
     * 액세스 토큰 갱신
     * 
     * @return
     */
    @PostMapping("/new/access")
    public ResponseEntity<RestResponse> getNewAccessToken(
       @RequestBody TokenRequest tokenRequest
    ) {
        
        TokenResponse tokenResponse = tokenService
            .getNewAccessToken(tokenRequest);
        
        jwtCookieUtil.addCookie(
            httpServletResponse, 
            CookieRequest.builder()
                .cookieValue(tokenResponse.getAccessToken())
                .cookieName(CookieNames.ACCESS_TOKEN)
                .maxAge(ExpirationTime.ACCESS_TOKEN_IN_SECONDS)
                .build()
        );
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("액세스 토큰 발급 성공")
            .data(tokenResponse)
            .build()
            .toResponse();
    }

}
