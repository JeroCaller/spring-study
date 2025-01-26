package com.jerocaller.exception;

/**
 * 사용자 회원가입 시 입력한 정보가 이미 DB 상에 존재하는 유저일 경우 일으킬 커스텀 예외 클래스.
 */
public class UserAlreadyExistException extends RuntimeException {
	
	public UserAlreadyExistException(String message) {
		super(message);
	}
	
}
