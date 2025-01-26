package com.jerocaller.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import com.jerocaller.dto.MembersRequest;
import com.jerocaller.dto.MembersResponse;
import com.jerocaller.util.AuthenticationUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인, 로그아웃을 위한 서비스
 * 
 * 세션 기반 인증 구현 시 참고할 사이트들) 
 * https://cornpip.tistory.com/114
 * https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html#store-authentication-manually
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final SecurityContextRepository securityContextRepository;
	
	public MembersResponse login(
		MembersRequest membersRequest,
		HttpServletRequest request,
		HttpServletResponse response
	) throws AuthenticationException {
		
		log.info("AuthService.login");
		log.info("member request 정보 확인");
		log.info("닉네임: " + membersRequest.getNickname());
		log.info("패스워드: " + membersRequest.getPassword());
		
		// 유저 닉네임 및 패스워드 기반으로 인증 토큰을 생성. 
		UsernamePasswordAuthenticationToken token = 
			new UsernamePasswordAuthenticationToken(
				membersRequest.getNickname(), 
				membersRequest.getPassword()
			);
		
		// 인증 매니저로 인증 시도. 내부적으로
		// CustomUserDetailService 클래스의 loadUserByUsername() 호출하여
		// 사용자 정보를 획득. 따라서 개발자가 별도로 CustomUserDetailService를 
		// 호출하여 사용할 필요가 없다고 함. 
		Authentication authentication = authenticationManager
			.authenticate(token);
			
		// 인증 성공 시 SecurityContextHolder에 인증 객체 전달.
		// 이제 어느 클래스건 SecurityContextHolder.getContext().getAuthentication()
		// 만 호출하면 현재 인증된 사용자 정보에 접근 가능.
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication); // 인증 정보를 세션에 저장.
		
		// 인증 정보가 포함된 세션을 서버에 영속화하여 다른 request가 와도 인증 정보를 유지하도록 한다.
		// 이 코드를 사용하지 않으면 인증을 하더라도 그 인증 정보가 유지되지 않아서 
		// 다른 request가 오면 다시 익명 사용자로 변경된다는 문제가 발생한다. 
		securityContextRepository.saveContext(securityContext, request, response);
		
		// for test
		log.info("현재 로그인한 사용자 정보: " + SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getName()
		);
		log.info("현재 로그인한 사용자 정보: " + AuthenticationUtils.getLoggedinUserInfo());
		
		return AuthenticationUtils.getLoggedinUserInfo();
		
	}
	
}
