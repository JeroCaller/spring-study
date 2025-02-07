package com.jerocaller.controller.old;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.MemberRegister;
import com.jerocaller.dto.old.ResponseJson;
import com.jerocaller.service.MemberServiceImpl;
import com.jerocaller.service.interf.MemberServiceinterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/old/members")
@RequiredArgsConstructor
public class MemberControllerOld {
	
	private final MemberServiceinterface<MemberServiceImpl> memberService;
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseJson> getMemberById(
		@PathVariable("id") int id
	) {
		
		ResponseJson responseJson = null;
		int result = 0;
		
		try {
			result = memberService.getById(id);
		} catch (EntityNotFoundException enfe) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message(enfe.getMessage())
				.build();
		} catch (RuntimeException re) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message(re.getMessage())
				.build();
		}
		
		if (responseJson == null) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.OK)
				.message("조회 성공")
				.data(result)
				.build();
		}
		
		return responseJson.toResponse();
		
	}
	
	@PostMapping("/registration")
	public ResponseEntity<ResponseJson> register(
		@Valid @RequestBody MemberRegister memberRegister
	) {
		
		// 기존 방식으로는 유효성 검사 실패 시 발생하는 예외를 처리할 방법이 없다!!!
		
		return ResponseJson.builder()
			.httpStatus(HttpStatus.CREATED)
			.message("회원 가입 성공")
			.data(memberRegister)
			.build()
			.toResponse();
	}

}
