package com.jerocaller.exception.classes;

import org.springframework.http.HttpStatus;

public class PasswordNotMatchedException extends BaseCustomException {
    
    public PasswordNotMatchedException() {
        super("패스워드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
    
}
