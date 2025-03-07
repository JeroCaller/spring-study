package com.jerocaller.data.dto.request;

import com.jerocaller.common.CookieNames;
import com.jerocaller.common.ExpirationTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에 응답 전송 시 같이 전송할 새 쿠키 생성에 필요한 
 * 인자들을 모은 DTO 클래스.
 * 
 * @see CookieUtil#addCookieWithJwt
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookieRequest {
    
    int maxAge;
    String cookieValue;
    String cookieName;
    
    public static CookieRequest toRequestForAccessToken(String accessToken) {
        
        return CookieRequest.builder()
            .cookieName(CookieNames.ACCESS_TOKEN)
            .cookieValue(accessToken)
            .maxAge(ExpirationTime.ACCESS_TOKEN_IN_SECONDS)
            .build();
    }
    
    public static CookieRequest toRequestForRefreshToken(String refreshToken) {
        
        return CookieRequest.builder()
            .cookieName(CookieNames.REFRESH_TOKEN)
            .cookieValue(refreshToken)
            .maxAge(ExpirationTime.REFRESH_TOKEN_IN_SECONDS)
            .build();
    }
    
}
