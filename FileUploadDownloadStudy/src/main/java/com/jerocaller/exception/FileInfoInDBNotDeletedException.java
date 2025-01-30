package com.jerocaller.exception;

/**
 * 파일 정보가 DB 상에서 삭제되지 않은 경우 발생시킬 커스텀 예외 클래스.
 */
public class FileInfoInDBNotDeletedException extends RuntimeException {
	
	public FileInfoInDBNotDeletedException(String reason) {
		super("파일 정보가 DB 내에서 삭제되지 않음. 원인 : \n" + reason);
	}
	
}
