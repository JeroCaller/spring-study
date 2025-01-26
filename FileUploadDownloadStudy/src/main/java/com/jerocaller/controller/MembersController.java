package com.jerocaller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.common.ResponseMessages;
import com.jerocaller.dto.MembersHistory;
import com.jerocaller.dto.MembersRequest;
import com.jerocaller.dto.MembersResponse;
import com.jerocaller.dto.ResponseJson;
import com.jerocaller.exception.NotAuthenticatedUserException;
import com.jerocaller.exception.UserAlreadyExistException;
import com.jerocaller.service.MembersService;
import com.jerocaller.util.AuthenticationUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MembersController {
	
	private final MembersService membersService;
	
	/**
	 * 현재 로그인한 사용자의 비민감 정보 반환.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ResponseJson> getCurrentMember() {
		
		ResponseJson responseJson = null;
		MembersResponse membersResponse = null;
		
		// For test
		Authentication authFromUtil = AuthenticationUtils.getAuth();
		log.info("현재 로그인한 사용자 닉네임 By utils: " + authFromUtil.getName());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.info("현재 로그인한 사용자 닉네임 By SecurityContextHolder: " + auth.getName());
		
		try {
			membersResponse = AuthenticationUtils.getLoggedinUserInfo();
		} catch (NotAuthenticatedUserException e) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message(e.getMessage())
				.build();
		}
		
		if (responseJson == null) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.OK)
				.message(ResponseMessages.READ_SUCCESS)
				.data(membersResponse)
				.build();
		}
		
		return responseJson.toResponseEntity();
		
	}
	
	/**
	 * 회원 가입
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ResponseJson> registerMember(
		@RequestBody MembersRequest request
	) {
		
		ResponseJson responseJson = null;
		MembersResponse membersResponse = null;
			
		try {
			membersResponse = membersService.register(request);
		} catch (IllegalArgumentException ie) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.BAD_REQUEST)
				.message("요청 정보가 null입니다. 제대로 된 정보를 입력해주세요.")
				.build();
		} catch (UserAlreadyExistException ue) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.CONFLICT)
				.message("이미 존재하는 아이디입니다. 다른 아이디를 사용해주세요.")
				.build();
		}
		
		if (responseJson == null) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("회원가입 성공")
				.data(membersResponse)
				.build();
		}
		
		return responseJson.toResponseEntity();
		
	}
	
	/**
	 * 현재 인증된 사용자의 회원 정보 수정
	 * 
	 * @return
	 */
	@PutMapping
	public ResponseEntity<ResponseJson> updateUserInfo(
		@RequestBody MembersRequest membersRequest,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		
		ResponseJson responseJson = null;
		MembersHistory membersHistory = null;
		
		try {
			membersHistory = membersService.update(membersRequest, request, response);
		} catch (NotAuthenticatedUserException e) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.UNAUTHORIZED)
				.message(e.getMessage())
				.build();
		}
		
		if (responseJson == null) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.OK)
				.message("회원 정보 수정 성공")
				.data(membersHistory)
				.build();
		}
		
		return responseJson.toResponseEntity();
	}
	
	/**
	 * 회원 탈퇴. 
	 * 현재 인증된 사용자에 한해서만 가능.
	 * 
	 * @return
	 */
	@DeleteMapping
	public ResponseEntity<ResponseJson> unregister(
		HttpServletRequest request,
		HttpServletResponse response
	) {
		
		ResponseJson responseJson = null;
		MembersResponse membersResponse = null;
		
		try {
			membersResponse = membersService.unregister(request, response);
		} catch (NotAuthenticatedUserException e) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.UNAUTHORIZED)
				.message(e.getMessage())
				.build();
		}
		
		if (responseJson == null) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.OK)
				.message("회원 탈퇴 성공")
				.data(membersResponse)
				.build();
		}
		
		return responseJson.toResponseEntity();
	}
	
}
