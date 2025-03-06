package com.jerocaller.exception.classes.jwt;

import io.jsonwebtoken.JwtException;

public class RefreshTokenExpiredException extends JwtException {
    
    public RefreshTokenExpiredException() {
        super("리프레시 토큰이 만료되었습니다. 재로그인이 필요합니다.");
    }
    
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
    
}
