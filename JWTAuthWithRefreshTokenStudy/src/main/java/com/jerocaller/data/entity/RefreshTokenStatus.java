package com.jerocaller.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RefreshTokenStatus {
    
    VALID("VALID"),
    INVALID("INVALID");
    
    private String tokenStatus;
    
}
