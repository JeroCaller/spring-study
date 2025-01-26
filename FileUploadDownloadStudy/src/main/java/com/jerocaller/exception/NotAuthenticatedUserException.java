package com.jerocaller.exception;

/**
 * 현재 사용자가 인증되지 않은 경우에 발생시킬 커스텀 예외 클래스.
 * 
 */
public class NotAuthenticatedUserException extends RuntimeException {
	
	public NotAuthenticatedUserException() {
		super("인증되지 않았습니다. 로그인해주세요.");
	}
	
	public NotAuthenticatedUserException(String message) {
		super(message);
	}
	
}
