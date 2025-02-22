package com.jerocaller.data.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestResponse {
    
    private HttpStatus httpStatus;
    private String message;
    private Object data;
    
    public ResponseEntity<RestResponse> toResponse() {
        
        return ResponseEntity.status(httpStatus)
            .body(this);
    }
    
}
