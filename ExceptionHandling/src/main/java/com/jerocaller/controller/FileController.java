package com.jerocaller.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.ApiResponse;
import com.jerocaller.dto.ApiResponseCustom;
import com.jerocaller.service.FileServiceCustomImpl;
import com.jerocaller.service.FileServiceImpl;
import com.jerocaller.service.interf.FileServiceInterface;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
	
	private final FileServiceInterface<FileServiceImpl> fileService;
	private final HttpServletRequest request;
	
	@GetMapping("/members/{memberId}")
	public ResponseEntity<Object> getMethodName(
		@PathVariable("memberId") int memberId
	) {
		
		List<Integer> filesId = fileService.getFilesIdByMember(memberId);
		
		// FileServiceImpl 전용
		return ApiResponse.builder()
			.httpStatus(HttpStatus.OK)
			.message("응답 성공")
			.uri(request.getRequestURI())
			.data(filesId)
			.build()
			.toResponse();
		
		// FileServiceCustomImpl 전용.
		/*
		return ApiResponseCustom.builder()
			.uri(request.getRequestURI())
			.data(filesId)
			.build()
			.toResponse();
		*/
	}
	
	// FileServiceImpl 전용
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<Object> handleException(
		RuntimeException e
	) {
		
		log.info("특정 컨트롤러 클래스 내에서 예외 처리됨.");
		
		return ApiResponse.builder()
			.httpStatus(HttpStatus.NOT_FOUND)
			.message(e.getMessage())
			.uri(request.getRequestURI())
			.build()
			.toResponse();
	}
	
}
