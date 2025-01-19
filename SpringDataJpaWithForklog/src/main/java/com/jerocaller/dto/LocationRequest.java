package com.jerocaller.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * 클라이언트로부터 도로명 주소 정보를 받기 위한 DTO 클래스.
 */
@Getter
@ToString
public class LocationRequest {
	
	private String large;
	private String middle;
	private String road;
	
}
