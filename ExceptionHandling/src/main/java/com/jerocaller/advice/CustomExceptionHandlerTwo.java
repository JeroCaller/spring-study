package com.jerocaller.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jerocaller.common.CustomResponseCode;
import com.jerocaller.dto.ApiResponse;
import com.jerocaller.dto.ApiResponseCustom;
import com.jerocaller.exception.CustomException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class CustomExceptionHandlerTwo {
	
	private final HttpServletRequest request;
	
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<Object> handleRuntimeException(
		RuntimeException e
	) {
		
		log.info("전역 예외 처리용 컨트롤러 어드바이스에서 {} 예외 처리됨", e.getClass().getName());
		
		return ApiResponse.builder()
			.httpStatus(HttpStatus.NOT_FOUND)
			.message(e.getMessage())
			.uri(request.getRequestURI())
			.build()
			.toResponse();
	}
	
	@ExceptionHandler(value = CustomException.class)
	public ResponseEntity<Object> handleCustomException(
		CustomException e
	) {
		
		log.info("커스텀 예외 처리용 컨트롤러 어드바이스에서 {} 예외 처리함.", e.getClass().getName());
		
		return ApiResponseCustom.builder()
			.responseCode(e.getResponseCode())
			.uri(request.getRequestURI())
			.build()
			.toResponse();
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationException(
		MethodArgumentNotValidException e
	) {
		
		log.info("컨트롤러 어드바이스에서 유효성 검사 예외 처리.");
		
		// key: field name, value : message
		Map<String, String> validationExceptionMsg 
			= new HashMap<String, String>();
		
		log.info("=== 유효성 검사 예외 관련 정보 ===");
		e.getBindingResult().getFieldErrors().forEach(error -> {
			log.info(error.getField());
			log.info(error.getDefaultMessage());
			log.info(error.getCode());
			log.info(error.getObjectName());
			
			validationExceptionMsg.put(
				error.getField(), 
				error.getDefaultMessage()
			);
		});
		log.info("=== 유효성 검사 예외 관련 정보 끝 ===");
		
		return ApiResponseCustom.builder()
			.responseCode(CustomResponseCode.MEMBER_REGISTER_BAD_REQUEST)
			.uri(request.getRequestURI())
			.validationMsg(validationExceptionMsg)
			.build()
			.toResponse();
		
	}
	
}
