package com.jerocaller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jerocaller.dto.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomExceptionHandler {
	
	private final HttpServletRequest request;
	
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<Object> handleException(
		RuntimeException e
	) {
		
		log.info("컨트롤러 어드바이스에서 예외 처리됨.");
		
		return ApiResponse.builder()
			.httpStatus(HttpStatus.NOT_FOUND)
			.message(e.getMessage())
			.uri(request.getRequestURI())
			.build()
			.toResponse();
	}
	
	/**
	 * EntityNotFoundException은 RuntimeException의 자손 클래스이다. 
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityException(
		EntityNotFoundException e
	) {
		
		log.info("컨트롤러 어드바이스에서 EntityNotFoundException 예외 처리됨.");
		
		return ApiResponse.builder()
			.httpStatus(HttpStatus.NOT_FOUND)
			.message(e.getMessage())
			.uri(request.getRequestURI())
			.build()
			.toResponse();	
	}
	
}
