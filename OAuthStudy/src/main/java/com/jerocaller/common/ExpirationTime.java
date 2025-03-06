package com.jerocaller.common;

public interface ExpirationTime {
    
    // 액세스 토큰 만료 시간 설정
    final int ACCESS_TOKEN_IN_SECONDS = 60 * 60 * 1;
    final long ACCESS_TOKEN_IN_MILLI_SECONDS = 1000L * ACCESS_TOKEN_IN_SECONDS;
    
    // 리프레시 토큰 만료 시간 설정
    final int REFRESH_TOKEN_IN_SECONDS = 60 * 60 * 24 * 1;
    final long REFRESH_TOKEN_IN_MILLI_SECONDS = 1000L * REFRESH_TOKEN_IN_SECONDS;
    
    // OAuth2 권한 요청 관련 상태 저장용 쿠키의 만료 시간 설정
    final int OAUTH2_AUTH_REQUEST_IN_SECONDS = 60 * 60 * 5;
    
}
