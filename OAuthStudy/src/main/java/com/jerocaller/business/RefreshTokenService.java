package com.jerocaller.business;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jerocaller.common.CookieNames;
import com.jerocaller.common.ExpirationTime;
import com.jerocaller.data.entity.Member;
import com.jerocaller.data.entity.RefreshToken;
import com.jerocaller.data.repository.RefreshTokenRepository;
import com.jerocaller.jwt.JwtTokenProvider;
import com.jerocaller.util.CookieUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil jwtCookieUtil;
    
    public RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> 
                new EntityNotFoundException("조회된 refresh token 없음.")
            );
    }
    
    @Transactional
    public RefreshToken createAndSaveRefreshToken(Member member) {
        
        String refreshToken = jwtTokenProvider.createToken(
            member, 
            ExpirationTime.REFRESH_TOKEN_IN_MILLI_SECONDS
        );
        
        // 리프레시 DB 테이블에 이미 해당 회원 레코드가 존재할 경우, 
        // 새로 생성하지 않고 해당 레코드에 업데이트.
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository
            .findByMember(member);
        
        RefreshToken newRefreshToken = null;
        
        if (refreshTokenOpt.isPresent()) {
            RefreshToken existRefreshToken = refreshTokenOpt.get();
            newRefreshToken = RefreshToken.builder()
                .id(existRefreshToken.getId())
                .refreshToken(refreshToken)
                .member(existRefreshToken.getMember())
                .build();
        } else {
            newRefreshToken = RefreshToken.builder()
                .refreshToken(refreshToken)
                .member(member)
                .build();
        }

        return refreshTokenRepository.save(newRefreshToken);
    }
    
    /**
     * 로그아웃 시 DB에 저장된 리프레시 토큰 삭제.
     * 
     * @param httpServletRequest
     */
    @Transactional
    public void deleteRefreshToken(HttpServletRequest httpServletRequest) {
        
        String token = jwtCookieUtil
            .extractJwtFromCookies(
                httpServletRequest, 
                CookieNames.REFRESH_TOKEN
            );
        
        RefreshToken targetRefreshToken = refreshTokenRepository
            .findByRefreshToken(token)
            .orElse(null);
        if (targetRefreshToken == null) return;
        
        refreshTokenRepository.delete(targetRefreshToken);
    }
    
}
