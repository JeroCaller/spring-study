package com.jerocaller.common;

/**
 * 컨트롤러에서 클라이언트에 응답 데이터 전송 시 함께 전송할 
 * 공통 응답 메시지 상수 모음.
 */
public interface ResponseMessages {
	
	final String READ_SUCCESS = "조회 성공";
	
	final String DELETE_SUCCESS = "삭제 성공";
	final String DELETE_FAILED = "삭제 실패";
	
	final String UNAUTHORIZED = "관련 권한이 없습니다.";
	
}
