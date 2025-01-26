package com.jerocaller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.MembersRequest;
import com.jerocaller.dto.MembersResponse;
import com.jerocaller.dto.ResponseJson;
import com.jerocaller.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자 인증을 위한 컨트롤러
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<ResponseJson> login(
		@RequestBody MembersRequest membersRequest,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		
		ResponseJson responseJson = null;
		MembersResponse membersResponse = null;
		
		try {
			membersResponse = authService.login(membersRequest, request, response);
		} catch (AuthenticationException e) {
			log.info("로그인 예외 발생");
			log.info(e.getMessage());
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.UNAUTHORIZED)
				.message(e.getMessage())
				.build();
		}
		
		if (responseJson == null) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.OK)
				.message("로그인 성공")
				.data(membersResponse)
				.build();
		}
		
		return responseJson.toResponseEntity();
		
	}
	
	@PostMapping("/logout")
	public ResponseEntity<ResponseJson> logout() {
		
		return ResponseJson.builder()
			.httpStatus(HttpStatus.OK)
			.message("로그아웃 성공")
			.build()
			.toResponseEntity();
		
	}
	
}
