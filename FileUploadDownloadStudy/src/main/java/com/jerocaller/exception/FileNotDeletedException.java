package com.jerocaller.exception;

/**
 * 파일 삭제 작업을 실행하였으나 삭제되지 않은 경우에 발생시킬 커스텀 예외 클래스.
 */
public class FileNotDeletedException extends RuntimeException {
	
	public FileNotDeletedException(String failedFileInfo) {
		super("파일이 삭제되지 않았습니다. 삭제 실패된 파일 정보: " + failedFileInfo);
	}
	
}
