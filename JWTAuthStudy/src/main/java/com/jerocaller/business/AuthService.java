package com.jerocaller.business;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jerocaller.data.dto.request.MemberRequest;
import com.jerocaller.data.dto.response.MemberResponse;
import com.jerocaller.data.entity.Member;
import com.jerocaller.exception.classes.PasswordNotMatchedException;
import com.jerocaller.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    
    public MemberResponse login(MemberRequest memberRequest) {
        
        // DB 내 해당 유저 아이디가 있는지 확인
        Member member = (Member) userDetailsService.loadUserByUsername(
            memberRequest.getUsername()
        );
        
        // 패스워드 일치 여부 검사
        if (!passwordEncoder.matches(
            memberRequest.getPassword(), 
            member.getPassword()
        )) {
            throw new PasswordNotMatchedException();
        }
        
        String newToken = jwtTokenProvider.createToken(
            member.getUsername(), member.getRole()
        );
        
        return MemberResponse.toDtoWithToken(member, newToken);
    }
    
}
