package com.jerocaller.exception;

/**
 * 파일을 찾을 수 없거나 읽을 수 없을 경우 발생시킬 커스텀 예외 클래스
 */
public class FileNotFoundOrReadableException extends RuntimeException {
	
	/**
	 * 
	 * @param message - 예외 메시지
	 * @param path - 예외를 발생시킨 문제의 경로
	 */
	public FileNotFoundOrReadableException(String message, String path) {
		super(message + " : " + path);
	}
	
}
