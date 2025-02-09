package com.jerocaller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.common.CustomResponseCode;
import com.jerocaller.dto.ApiResponse;
import com.jerocaller.dto.ApiResponseCustom;
import com.jerocaller.dto.MemberRegister;
import com.jerocaller.service.MemberServiceCustomImpl;
import com.jerocaller.service.MemberServiceImpl;
import com.jerocaller.service.interf.MemberServiceinterface;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberServiceinterface<MemberServiceCustomImpl> memberService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getMethodName(
		@PathVariable("id") int id,
		HttpServletRequest request
	) {
		
		int result = memberService.getById(id);
		
		// MemberServiceImpl 전용
		/*
		return ApiResponse.builder()
			.httpStatus(HttpStatus.OK)
			.message("응답 성공")
			.uri(request.getRequestURI())
			.data(result)
			.build()
			.toResponse();
		*/
		
		// MemberServiceCustomImpl 전용
		return ApiResponseCustom.builder()
			.uri(request.getRequestURI())
			.data(result)
			.build()
			.toResponse();
	}
	
	// MemberServiceCustomImpl 전용
	@PostMapping("/registration")
	public ResponseEntity<Object> register( 
		@Valid @RequestBody MemberRegister register,
		HttpServletRequest request
	) {
		
		return ApiResponseCustom.builder()
			.responseCode(CustomResponseCode.MEMBER_CREATED)
			.uri(request.getRequestURI())
			.data(register)
			.build()
			.toResponse();
	}
	
}
