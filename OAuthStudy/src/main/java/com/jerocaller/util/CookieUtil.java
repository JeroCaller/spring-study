package com.jerocaller.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.jerocaller.data.dto.request.CookieRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {
    
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
    
    public void addCookie(
        HttpServletResponse httpServletResponse,
        CookieRequest cookieRequest
    ) {
        
        Cookie cookie = new Cookie(
            cookieRequest.getCookieName(), 
            cookieRequest.getCookieValue()
        );
        
        // 클라이언트의 조작에 의한 변형 방지. XSS 공격 방지
        cookie.setHttpOnly(true);
        
        // HTTPS에서만 사용할 경우 true로 설정. HTTP에서도 사용할 경우 false.
        cookie.setSecure(false);
        
        // 전체 경로에서 사용 가능.
        cookie.setPath("/");
        
        cookie.setMaxAge(cookieRequest.getMaxAge());
        httpServletResponse.addCookie(cookie);
        
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
    
    /**
     * 객체 직렬화 후 쿠키의 값으로 변환
     * 
     * @param obj
     * @return
     */
    public String serialize(Object obj) {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(obj));
    }
    
    public <T> T deserialize(Cookie cookie, Class<T> cls) {
        
        byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());
        Object deserializedObj = SerializationUtils.deserialize(decodedBytes);
        return cls.cast(deserializedObj);
    }
    
}
