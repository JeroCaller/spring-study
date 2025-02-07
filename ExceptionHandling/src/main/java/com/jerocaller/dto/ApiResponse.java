package com.jerocaller.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에게 JSON 형식으로 응답하기 위한 DTO 클래스.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {
	
	private HttpStatus httpStatus;
	
	/**
	 * 응답 상세 메시지
	 */
	private String message;
	
	/**
	 * 클라이언트가 요청한 URI
	 */
	private String uri;
	
	/**
	 * 응답 데이터
	 */
	private Object data;
	
	/**
	 * HTTP Status의 숫자 코드와 메시지를 분리하여 응답하기 위한 
	 * 응답 클래스.
	 */
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class DetailedApiResponse {
		
		private int code;
		private String type;
		
		private String message;
		private String uri;
		private Object data;
		
	}
	
	public ResponseEntity<Object> toResponse() {
		
		DetailedApiResponse response = DetailedApiResponse.builder()
			.code(this.getHttpStatus().value())
			.type(this.getHttpStatus().getReasonPhrase())
			.message(this.getMessage())
			.uri(this.getUri())
			.data(this.getData())
			.build();
		
		return ResponseEntity.status(httpStatus)
			.body(response);
	}

}
