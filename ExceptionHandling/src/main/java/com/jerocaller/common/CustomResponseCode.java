package com.jerocaller.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 클라이언트에 전송할 커스텀 응답 코드 모음. 
 */
@Getter
@AllArgsConstructor
public enum CustomResponseCode {
	
	// 기본값 용도. 다른 값으로 초기화해야함.
	DEFAULT(
		HttpStatus.INTERNAL_SERVER_ERROR, 
		"DEFAULT", 
		"기본값 용도. 다른 값으로 초기화해야 함."
	),
	
	// 응답 상태 양호 코드
	OK(HttpStatus.OK, "OK", "응답 성공"),
	MEMBER_CREATED(HttpStatus.CREATED, "MEMBER_REGISTER", "회원 가입 성공"),
	
	// 응답 에러 코드
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "조회된 회원 없음."),
	MEMBER_REGISTER_BAD_REQUEST(
		HttpStatus.BAD_REQUEST, 
		"ACCOUNT-002", 
		"잘못된 형식의 회원 가입 정보입니다."
	),
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE-001", "조회된 파일 없음.");
	
	// 필드 정의
	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
