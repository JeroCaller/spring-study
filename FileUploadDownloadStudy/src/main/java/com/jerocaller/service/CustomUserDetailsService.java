package com.jerocaller.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jerocaller.entity.Members;
import com.jerocaller.repository.MembersRepository;

import lombok.RequiredArgsConstructor;

/**
 * 인증을 위해 사용자 입력 로그인 정보가 DB 상에 존재하는지 확인하는 클래스. 
 * 이 클래스는 보통 UserDetailsService를 구현하며, loadUserByUsername 메서드 구현 시 
 * 보통 사용자 아이디 등의 정보가 넘어오면 이를 JpaRepository를 통해 조회한 후, 
 * 존재하면 인증 성공, 존재하지 않으면 인증 실패로 결정하도록 개발자가 구현하면 되는 것으로 보임. 
 * 
 * AuthenticationManager.authenticate(token); 메서드 내부에서 이 객체를 호출하여 
 * 사용자 인증을 한다고 한다. 따라서 이 경우 개발자가 따로 이 객체를 호출하여 별도의 사용자 인증을 
 * 거칠 필요가 없다고 한다. 
 * 
 * 만약 현재 인증된 사용자의 추가 정보들도 같이 반환되었으면 좋겠다 싶으면 
 * UserDetails 인터페이스를 구현한 별도의 클래스를 작성하여 그 안에 포함시키면 된다고 한다. 
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final MembersRepository membersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) 
		throws UsernameNotFoundException 
	{
		
		Members member = membersRepository.findById(username)
			.orElseThrow(() -> new UsernameNotFoundException("해당 유저 없음."));
		
		// 조회 시에는 패스워드를 암호화하지 않는다. 
		// 회원 가입, 수정 등 DB에 패스워드를 저장할 때에만 암호화하여 저장되도록 함. 
		// 따라서 DB로부터 패스워드 읽을 때에는 이미 암호화된 상태이므로 그냥 읽어들인다. 
		return User.builder()
			.username(username)
			//.password(passwordEncoder.encode(member.getPassword()))
			.password(member.getPassword())
			.build();
		
	}
	
}
