package com.jerocaller.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.jerocaller.dto.MembersHistory;
import com.jerocaller.dto.MembersRequest;
import com.jerocaller.dto.MembersResponse;
import com.jerocaller.entity.Members;
import com.jerocaller.exception.NotAuthenticatedUserException;
import com.jerocaller.exception.UserAlreadyExistException;
import com.jerocaller.repository.MembersRepository;
import com.jerocaller.util.AuthenticationUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MembersService {

	private final MembersRepository membersRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService customUserDetailsService;
	private final AuthService authService;
	private final SecurityContextLogoutHandler securityContextLogoutHandler;
	
	/**
	 * 회원 가입 메서드.
	 * 
	 * @param request
	 * @return
	 */
	public MembersResponse register(MembersRequest request) 
		throws UserAlreadyExistException {
		
		// 사용자가 입력한 회원 가입 정보가 이미 DB 상에 존재하는지 확인. 
		// 존재 시 예외를 던지며 메서드를 종료한다. 
		try {
			customUserDetailsService
				.loadUserByUsername(request.getNickname());
			
			throw new UserAlreadyExistException("이미 존재하는 회원입니다.");
		} catch (UsernameNotFoundException e) {
			// Empty Code. Continue;
		}
		
		String encodedPassword = passwordEncoder.encode(request.getPassword());
		log.info("암호화 패스워드 길이: " + encodedPassword.length());
		log.info(encodedPassword);
		
		// 패스워드는 암호화하여 DB에 저장하도록 함. 
		Members willBeSavedMember = Members.builder()
			.nickname(request.getNickname())
			.password(encodedPassword)
			.build();
		Members registeredMember = membersRepository.save(willBeSavedMember);
		
		return MembersResponse.toDto(registeredMember);
		
	}
	
	/**
	 * 회원 정보 수정.
	 * 
	 * @param request - 새로 수정할 회원 정보
	 * @param pastUserInfo - 기존 회원 정보
	 * @return
	 */
	public MembersHistory update(
		MembersRequest membersRequest, 
		HttpServletRequest request,
		HttpServletResponse response
	) throws NotAuthenticatedUserException {
		
		// 현재 인증된 정보가 없으면 런타임 예외 발생하여 throw함.
		// 현재 인증된 사용자인지 판별과 동시에 기존 회원 정보 반환을 위한 코드.
		UserDetails pastUserInfo = AuthenticationUtils
			.getAuthedUserInfoAll();
		
		// 수정 전 정보 삭제
		membersRepository.deleteById(pastUserInfo.getUsername());
		
		// 수정 후 정보 삽입
		// 수정 후 패스워드는 암호화하여 DB에 저장한다.
		Members willBeUpdatedMember = Members.builder()
			.nickname(membersRequest.getNickname())
			.password(passwordEncoder.encode(membersRequest.getPassword()))
			.build();
		Members updatedMember = membersRepository.save(willBeUpdatedMember);
		
		// 세션에 저장된 인증 정보를 수정 후 정보로 갱신
		authService.login(membersRequest, request, response);
		
		return MembersHistory.builder()
			.pastNickname(pastUserInfo.getUsername())
			.newNickname(updatedMember.getNickname())
			.build();
	}
	
	/**
	 * 회원 탈퇴 기능
	 * 
	 * 회원 탈퇴 후 세션 기능 무효화 기능 관련 참고 사이트)
	 * https://docs.spring.io/spring-security/reference/servlet/authentication/logout.html#creating-custom-logout-endpoint
	 * 
	 * @return
	 */
	public MembersResponse unregister(
		HttpServletRequest request, 
		HttpServletResponse response
	) throws NotAuthenticatedUserException {
		
		// 현재 인증된 사용자 정보 없을 시 예외 throw하고 종료됨.
		UserDetails currentUserInfo = AuthenticationUtils.getAuthedUserInfoAll();
		membersRepository.deleteById(currentUserInfo.getUsername());
		
		// 세션에 저장되어 있는 인증 정보 무효화.
		securityContextLogoutHandler.logout(
			request, 
			response, 
			AuthenticationUtils.getAuth()
		);
		
		return MembersResponse.builder()
			.nickname(currentUserInfo.getUsername())
			.build();
		
	}
	
}
