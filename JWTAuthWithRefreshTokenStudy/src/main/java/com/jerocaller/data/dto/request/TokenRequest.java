package com.jerocaller.data.dto.request;

import lombok.Getter;

/**
 * access token 만료 시 갱신을 위한 DTO 클래스.
 */
@Getter
public class TokenRequest {
    
    private String refreshToken;
    
}
