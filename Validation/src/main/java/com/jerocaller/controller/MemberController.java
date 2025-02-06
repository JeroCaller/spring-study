package com.jerocaller.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.MemberRequest;
import com.jerocaller.dto.MemberRequestThree;
import com.jerocaller.dto.MemberRequestTwo;
import com.jerocaller.dto.RegisterRequest;
import com.jerocaller.validation.ValidationGroupOne;
import com.jerocaller.validation.ValidationGroupTwo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
	
	/**
	 * 유효성 검사 성공 예)
	 * {
	 *   "nickname": "kimquel1234",
	 *   "email": "sql@mymail.net",
	 *   "birthDate": "1990-03-04",
	 *   "phone": "010-1111-2222",
	 *   "age": 24,
	 *   "areYouNew": true,
	 *   "couponCode": 1073
	 * }
	 * 
	 * @param memberRequest
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Object> register(
		@Valid @RequestBody MemberRequest memberRequest
	) {
		
		logRegisterRequest(memberRequest);
		
		return ResponseEntity.ok(memberRequest);
		
	}
	
	@PostMapping("/validation")
	public ResponseEntity<Object> registerValidation(
		@Validated @RequestBody MemberRequestTwo request
	) {
		
		logRegisterRequest(request);
		
		return ResponseEntity.ok(request);
		
	}
	
	@PostMapping("/validation/group/one")
	public ResponseEntity<Object> registerValidationOne(
		@Validated(ValidationGroupOne.class) 
		@RequestBody MemberRequestTwo request
	) {
		
		logRegisterRequest(request);
		
		return ResponseEntity.ok(request);
		
	}
	
	@PostMapping("/validation/group/two")
	public ResponseEntity<Object> registerValidationTwo(
		@Validated(ValidationGroupTwo.class)
		@RequestBody MemberRequestTwo request
	) {
		
		logRegisterRequest(request);
		
		return ResponseEntity.ok(request);

	}
	
	@PostMapping("/validation/custom")
	public ResponseEntity<Object> registerCustomValidation(
		@Valid @RequestBody MemberRequestThree request
	) {
		
		logRegisterRequest(request);
		
		return ResponseEntity.ok(request);
		
	}
	
	private void logRegisterRequest(RegisterRequest request) {
		log.info("=== 새 회원 가입 요청 결과 ===");
		log.info(request.toString());
	}
	
}
