package com.jerocaller.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에 응답 전송 시 같이 전송할 새 쿠키 생성에 필요한 
 * 인자들을 모은 DTO 클래스.
 * 
 * @see JwtCookieUtil#addCookieWithJwt
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtCookieRequest {
    
    int maxAge;
    String token;
    String cookieName;
    
}
