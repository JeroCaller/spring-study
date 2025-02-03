package com.jerocaller.controller;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.common.ResponseMessages;
import com.jerocaller.dto.FilesLogging;
import com.jerocaller.dto.MembersHistory;
import com.jerocaller.dto.MembersRequest;
import com.jerocaller.dto.MembersResponse;
import com.jerocaller.dto.ResponseJson;
import com.jerocaller.exception.DirectoryDeleteFailedException;
import com.jerocaller.exception.NotAuthenticatedUserException;
import com.jerocaller.exception.UserAlreadyExistException;
import com.jerocaller.service.MembersService;
import com.jerocaller.util.AuthenticationUtils;

import jakarta.persistence.EntityNotFoundException;
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
	private final FilesLogging filesLogging;
	
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
		} catch (EntityNotFoundException ee) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message(ee.getMessage())
				.build();
		} catch (IOException ioe) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.message(ioe.getMessage())
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
		} catch (EntityNotFoundException ee) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message(ee.getMessage())
				.build();
		} catch (DirectoryNotEmptyException dnee) {
			// 해당 유저의 파일들을 담던 디렉토리 삭제 실패의 경우. 
			// 회원 탈퇴 자체는 진행되었기에 성공으로 간주
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.PARTIAL_CONTENT)
				.message(dnee.getMessage())
				.data(membersResponse)
				.build();
		} catch (DirectoryDeleteFailedException ddfe) {
			// 해당 유저의 파일들을 담던 디렉토리 삭제 실패의 경우. 
			// 회원 탈퇴 자체는 진행되었기에 성공으로 간주
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.PARTIAL_CONTENT)
				.message(ddfe.getMessage())
				.data(membersResponse)
				.build();
		}
		
		if (responseJson == null) {
			if (filesLogging.getFailedPaths().size() > 0) {
				// 회원이 보유한 일부 파일 삭제 실패한 경우.
				// 그럼에도 회원 탈퇴 절차 자체는 진행되어야 하기에 
				// 회원 탈퇴 자체는 성공했다고 응답.
				responseJson = ResponseJson.builder()
					.httpStatus(HttpStatus.PARTIAL_CONTENT)
					.message("탈퇴할 회원이 보유한 파일들 중 일부가 삭제 실패했습니다. 탈퇴 자체는 성공했습니다.")
					.data(filesLogging)
					.build();
			} else {
				responseJson = ResponseJson.builder()
					.httpStatus(HttpStatus.OK)
					.message("회원 탈퇴 성공")
					.data(membersResponse)
					.build();
			}
			
			log.info("=== 삭제 성공 파일 경로들 ===");
			for (String filePath : filesLogging.getSucceedFileNames()) {
				log.info(filePath);
			}
		}
		
		return responseJson.toResponseEntity();
	}
	
	/**
	 * 주어진 닉네임이 이미 DB 상에 존재하는지 여부를 반환.
	 * 
	 * @param nickname
	 * @return
	 */
	@GetMapping("/check")
	public ResponseEntity<ResponseJson> existMember(
		@RequestParam("nickname") String nickname
	) {
		
		boolean result = membersService.existsMemberBy(nickname);
		
		return ResponseJson.builder()
			.httpStatus(HttpStatus.OK)
			.message(ResponseMessages.READ_SUCCESS)
			.data(result)
			.build()
			.toResponseEntity();
		
	}
	
}
