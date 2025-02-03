package com.jerocaller.exception;

/**
 * 디렉토리 삭제 실패 시 일으킬 커스텀 예외 클래스.
 */
public class DirectoryDeleteFailedException extends RuntimeException {
	
	public DirectoryDeleteFailedException(String reason) {
		super("디렉토리 삭제 실패. 실패 원인 : \n" + reason);
	}
	
}
