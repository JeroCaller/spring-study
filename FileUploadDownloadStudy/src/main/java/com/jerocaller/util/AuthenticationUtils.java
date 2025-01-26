package com.jerocaller.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.jerocaller.dto.MembersResponse;
import com.jerocaller.exception.NotAuthenticatedUserException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationUtils {
	
	public static Authentication getAuth() {
		return SecurityContextHolder.getContext()
			.getAuthentication();
	}
	
	/**
	 * 인증 성공한 현재 사용자의 비민감 정보 반환.
	 * 
	 * @return
	 */
	public static MembersResponse getLoggedinUserInfo() 
		throws NotAuthenticatedUserException 
	{
		
		Authentication auth = SecurityContextHolder.getContext()
			.getAuthentication();
		
		log.info("사용자 정보 출력 ---");
		auth.getAuthorities().forEach(authz -> {
			log.info(authz.getAuthority());
			if (authz.getAuthority().equals("ROLE_ANONYMOUS")) {
				throw new NotAuthenticatedUserException();
			}
		});
		
		if (auth == null || !auth.isAuthenticated()) {
			throw new NotAuthenticatedUserException();
		}
		
		return MembersResponse.builder()
			.nickname(auth.getName())
			.build();
		
	}
	
	/**
	 * 현재 인증된 사용자의 민감 정보 포함 데이터 반환. 
	 * 별도로 DB를 거쳐 조회하지 않아도 되도록 고안함. 
	 * 
	 * @return
	 */
	public static UserDetails getAuthedUserInfoAll() 
		throws NotAuthenticatedUserException 
	{
		
		Authentication auth = SecurityContextHolder.getContext()
			.getAuthentication();
		
		if (auth == null || !auth.isAuthenticated()) {
			throw new NotAuthenticatedUserException();
		}
		
		return (UserDetails) auth.getPrincipal();
		
	}
	
}
