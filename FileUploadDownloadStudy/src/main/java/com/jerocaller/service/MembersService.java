package com.jerocaller.service;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.jerocaller.dto.MembersHistory;
import com.jerocaller.dto.MembersRequest;
import com.jerocaller.dto.MembersResponse;
import com.jerocaller.entity.Members;
import com.jerocaller.exception.DirectoryDeleteFailedException;
import com.jerocaller.exception.NotAuthenticatedUserException;
import com.jerocaller.exception.UserAlreadyExistException;
import com.jerocaller.repository.MembersRepository;
import com.jerocaller.util.AuthenticationUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 회원의 닉네임을 PK로 정하는 것은 좋지 않아보임. 차라리 고유 번호를 PK로 정하는 게 좋을 것 같음. 
 * 그 이유는 다음과 같음.
 * 
 * 1. 회원 닉네임 수정 시 닉네임이 PK이고, 파일과 FK로 연결되어 있기에 참조 무결성을 
 * 지키기 위해서는 어쩔 수 없이 새 회원 정보 데이터를 넣고, 기존 회원 정보 데이터를 삭제해야함. 
 * 이로 인해 상대적으로 코드 양이 더 많아지고, 회원 정보 수정 시각 내역을 기록할 수 없음. 
 * 
 * 회원 닉네임을 PK로 정했던 이유는, 고유 번호를 PK로 정해도 실상 닉네임으로 데이터 조회가 더 자주 
 * 일어날 것 같아 이 경우 PK 적용으로 같이 적용되어 있는 인덱스를 통한 검색 성능 향상을 노릴 수 
 * 없기 때문. 그러나 상기한 문제점으로 인해 차라리 고유 번호를 PK로 정한 후, 닉네임 필드에 
 * unique 키를 설정하여 닉네임을 통한 검색에서도 검색 성능 향상을 도모하는 것이 좋을 듯 싶음. 
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MembersService {

	private final MembersRepository membersRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService customUserDetailsService;
	private final AuthService authService;
	private final FileByMemberService fileByMemberService;
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
	 * @throws IOException 
	 */
	public MembersHistory update(
		MembersRequest membersRequest, 
		HttpServletRequest request,
		HttpServletResponse response
	) throws 
		NotAuthenticatedUserException, 
		EntityNotFoundException, 
		IOException 
	{
		
		// 현재 인증된 정보가 없으면 런타임 예외 발생하여 throw함.
		// 현재 인증된 사용자인지 판별과 동시에 기존 회원 정보 반환을 위한 코드.
		UserDetails pastUserInfo = AuthenticationUtils
			.getAuthedUserInfoAll();
		
		// 수정 전 회원 정보
		Members oldMember = membersRepository
			.findById(pastUserInfo.getUsername())
			.orElseThrow(() -> {
				String message = "조회된 수정 전 닉네임을 찾을 수 없습니다.";
				return new EntityNotFoundException(message);
			});
		
		// 수정 후 정보 삽입
		// 수정 후 패스워드는 암호화하여 DB에 저장한다.
		Members willBeUpdatedMember = Members.builder()
			.nickname(membersRequest.getNickname())
			.password(passwordEncoder.encode(membersRequest.getPassword()))
			.build();
		Members updatedMember = membersRepository.save(willBeUpdatedMember);
		
		// 수정 전 닉네임 정보를 가지고 있는 모든 파일들의 닉네임 정보를 
		// 수정 후 정보로 교체.
		fileByMemberService.updateUserInfoInFiles(oldMember, updatedMember);
		
		// 수정 전 정보 삭제
		//membersRepository.deleteById(pastUserInfo.getUsername());
		membersRepository.delete(oldMember);
		
		// 세션에 저장된 인증 정보를 수정 후 정보로 갱신
		authService.login(membersRequest, request, response);
		
		return MembersHistory.builder()
			.pastNickname(pastUserInfo.getUsername())
			.newNickname(updatedMember.getNickname())
			//.newNickname(willBeUpdatedMember.getNickname())
			.build();
		
	}
	
	/**
	 * 회원 탈퇴 기능
	 * 
	 * 회원 탈퇴 후 세션 기능 무효화 기능 관련 참고 사이트)
	 * https://docs.spring.io/spring-security/reference/servlet/authentication/logout.html#creating-custom-logout-endpoint
	 * 
	 * @return
	 * @throws IOException 
	 * @throws DirectoryNotEmptyException 
	 */
	public MembersResponse unregister(
		HttpServletRequest request, 
		HttpServletResponse response
	) throws 
		NotAuthenticatedUserException, 
		EntityNotFoundException, 
		DirectoryNotEmptyException, 
		DirectoryDeleteFailedException 
	{
		
		// 현재 인증된 사용자 정보 없을 시 예외 throw하고 종료됨.
		UserDetails currentUserInfo = AuthenticationUtils.getAuthedUserInfoAll();
		
		// 회원이 보유한 모든 파일 삭제
		fileByMemberService.deleteAllFilesByUsername(currentUserInfo.getUsername());
		
		// 회원 정보 DB 상에서 삭제
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
	
	/**
	 * 주어진 닉네임이 이미 DB 상에 존재하는지 여부를 반환.
	 * 
	 * @param nickname
	 * @return
	 */
	public boolean existsMemberBy(String nickname) {
		return membersRepository.existsById(nickname);
	}
	
}
