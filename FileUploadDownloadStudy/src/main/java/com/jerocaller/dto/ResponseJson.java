package com.jerocaller.dto;

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
public class ResponseJson {
	
	private HttpStatus httpStatus;
	private String message;
	private Object data;
	
	public ResponseEntity<ResponseJson> toResponseEntity() {
		
		return ResponseEntity.status(httpStatus)
				.body(this);
		
	}
	
}
