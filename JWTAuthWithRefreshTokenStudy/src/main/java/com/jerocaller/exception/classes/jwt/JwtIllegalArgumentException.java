package com.jerocaller.exception.classes.jwt;

import io.jsonwebtoken.JwtException;

/**
 * 주어진 JWT값이 비어있거나 null인 경우 발생시킬 커스텀 예외 클래스.
 */
public class JwtIllegalArgumentException extends JwtException{
    
    public JwtIllegalArgumentException() {
        super("주어진 JWT 값이 비어있거나 null값입니다. JWT값을 입력하세요.");
    }
    
    public JwtIllegalArgumentException(String message) {
        super(message);
    }
    
}
