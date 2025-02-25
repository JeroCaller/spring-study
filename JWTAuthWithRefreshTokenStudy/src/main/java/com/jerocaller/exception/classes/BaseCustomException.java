package com.jerocaller.exception.classes;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * 최상위 커스텀 예외 클래스. 커스텀 예외 클래스 정의 시 이 클래스를 상속하여 정의한다.
 */
@Getter
public class BaseCustomException extends RuntimeException {
    
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    
    public BaseCustomException() {
        super();
    }
    
    public BaseCustomException(String message) {
        super(message);
    }
    
    public BaseCustomException(HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }
    
    public BaseCustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    
}
