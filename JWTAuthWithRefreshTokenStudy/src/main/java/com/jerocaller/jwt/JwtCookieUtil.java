package com.jerocaller.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jerocaller.data.dto.request.JwtCookieRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtCookieUtil {
    
    public String extractJwtFromCookies(
        HttpServletRequest request, 
        String jwtCookieName
    ) {
        
        String token = null;
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(jwtCookieName)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        
        return token;
    }
    
    public void addCookieWithJwt(
        HttpServletResponse httpServletResponse,
        JwtCookieRequest jwtCookieRequest
    ) {
        
        Cookie jwtCookie = new Cookie(
            jwtCookieRequest.getCookieName(), 
            jwtCookieRequest.getToken()
        );
        
        // 클라이언트의 조작에 의한 변형 방지. XSS 공격 방지
        jwtCookie.setHttpOnly(true);
        
        // HTTPS에서만 사용할 경우 true로 설정. HTTP에서도 사용할 경우 false.
        jwtCookie.setSecure(false);
        
        // 전체 경로에서 사용 가능.
        jwtCookie.setPath("/");
        
        jwtCookie.setMaxAge(jwtCookieRequest.getMaxAge());
        httpServletResponse.addCookie(jwtCookie);
        
    }
    
    public void deleteCookies(
        HttpServletResponse httpResponse, 
        String... cookieNames
    ) {
        
        List<Cookie> cookies = new ArrayList<Cookie>();
        for (String cookieName : cookieNames) {
            Cookie cookie = new Cookie(cookieName, null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookies.add(cookie);
        }
        
        cookies.forEach(cookie -> {
           httpResponse.addCookie(cookie);
        });
        
    }
    
}
