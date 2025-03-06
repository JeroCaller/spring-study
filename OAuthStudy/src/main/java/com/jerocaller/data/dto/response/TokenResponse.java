package com.jerocaller.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 최초 로그인 또는 access token 또는 refresh token 갱신 시 
 * 클라이언트 응답용 DTO 클래스.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    
    private String accessToken;
    private String refreshToken;
    
    public static TokenResponse toDto(String accessToken) {
        return TokenResponse.builder()
            .accessToken(accessToken)
            .build();
    }
    
    public static TokenResponse toDto(
        String accessToken, 
        String refreshToken
    ) {
        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
    
}
