package com.jerocaller.exception.security;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 인증, 인가 등 Spring Security에서 발생한 예외에 대한 응답 데이터 생성용 
 * DTO 클래스.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SecurityExceptionResponse {
    
    private HttpStatus status;
    private String messasge;
    
}
