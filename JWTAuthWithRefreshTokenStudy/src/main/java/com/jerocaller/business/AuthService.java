package com.jerocaller.business;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.jerocaller.data.dto.request.MemberRequest;
import com.jerocaller.data.dto.response.MemberResponse;
import com.jerocaller.data.dto.response.TokenResponse;
import com.jerocaller.data.entity.Member;
import com.jerocaller.exception.classes.PasswordNotMatchedException;
import com.jerocaller.util.AuthUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final SecurityContextLogoutHandler logoutHandler;
    private final AuthUtil authUtil;
    
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
        
        // 액세스 및 리프레시 토큰 발급.
        TokenResponse tokenResponse = tokenService.getNewAllTokens(member);
        
        return MemberResponse.toDtoWithToken(member, tokenResponse);
    }
    
    public void logout(
        HttpServletRequest httpRequest, 
        HttpServletResponse httpResponse
    ) {
        
        // 로그아웃 전 DB에 저장한 리프레시 토큰 데이터 변경.
        // 삭제할 수도, 사용 가능 여부 상태를 바꿀 수도 있다. 
        // 여기서는 후자를 택함. 
        refreshTokenService.invalidateRefreshToken(httpRequest);
        
        logoutHandler.logout(httpRequest, httpResponse, authUtil.getAuth());
        
    }
    
}
