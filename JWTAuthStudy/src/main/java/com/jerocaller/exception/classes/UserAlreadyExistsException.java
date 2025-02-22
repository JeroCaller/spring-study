package com.jerocaller.exception.classes;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseCustomException {
    
    public UserAlreadyExistsException() {
        super("이미 존재하는 유저입니다.", HttpStatus.BAD_REQUEST);
    }
    
}
