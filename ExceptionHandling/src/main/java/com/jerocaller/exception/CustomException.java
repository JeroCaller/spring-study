package com.jerocaller.exception;

import com.jerocaller.common.CustomResponseCode;

import lombok.Getter;

/**
 * 커스텀 예외 클래스의 최상위 부모 클래스. 
 * 모든 커스텀 예외 클래스들은 이 클래스를 상속받아 정의하면 됨.
 */
@Getter
public class CustomException extends RuntimeException {
	
	private static final long serialVersionUID = 3578550418308671229L;
	
	protected CustomResponseCode responseCode;

	public CustomException() {
		super();
		this.responseCode = CustomResponseCode.DEFAULT;
	}
	
	public CustomException(CustomResponseCode responseCode) {
		super();
		this.responseCode = responseCode;
	}
	
	public CustomException(String message) {
		super(message);
		this.responseCode = CustomResponseCode.DEFAULT;
	}

}
