package com.jerocaller.jwt;

import org.springframework.stereotype.Component;

import com.jerocaller.common.CookieNames;
import com.jerocaller.common.ExpirationTime;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtCookieUtil {
    
    public String extractJwtFromCookies(HttpServletRequest request) {
        
        String token = null;
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CookieNames.JWT)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        
        return token;
    }
    
    public void addCookieWithJwt(String token, HttpServletResponse response) {
        
        Cookie jwtCookie = new Cookie(CookieNames.JWT, token);
        
        // 클라이언트의 조작에 의한 변형 방지. XSS 공격 방지
        jwtCookie.setHttpOnly(true);
        
        // HTTPS에서만 사용할 경우 true로 설정. HTTP에서도 사용할 경우 false.
        jwtCookie.setSecure(false);
        
        // 전체 경로에서 사용 가능.
        jwtCookie.setPath("/");
        
        jwtCookie.setMaxAge(ExpirationTime.inSeconds);
        response.addCookie(jwtCookie);
        
    }
    
}
