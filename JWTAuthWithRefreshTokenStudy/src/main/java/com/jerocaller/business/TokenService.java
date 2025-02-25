package com.jerocaller.business;

import org.springframework.stereotype.Service;

import com.jerocaller.common.ExpirationTime;
import com.jerocaller.data.dto.request.TokenRequest;
import com.jerocaller.data.dto.response.TokenResponse;
import com.jerocaller.data.entity.Member;
import com.jerocaller.data.entity.RefreshToken;
import com.jerocaller.exception.classes.jwt.RefreshTokenExpiredException;
import com.jerocaller.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    
    public TokenResponse getNewAccessToken(
        TokenRequest refreshTokenRequest
    ) {
        
        if (!jwtTokenProvider.validateToken(
            refreshTokenRequest.getRefreshToken()
        )) {
            throw new RefreshTokenExpiredException();
        }
        
        RefreshToken refreshToken = refreshTokenService
            .findRefreshToken(refreshTokenRequest.getRefreshToken());
        
        Member member = refreshToken.getMember();
        String newAccessToken = jwtTokenProvider.createToken(
            member, 
            ExpirationTime.ACCESS_TOKEN_IN_MILLI_SECONDS
        );
        
        return TokenResponse.toDto(newAccessToken);
    }
    
    /**
     * 액세스 토큰과 리프레시 토큰 모두 새로 발급한다. 
     * 로그인 시 발급할 용도.
     * 
     * @return
     */
    public TokenResponse getNewAllTokens(Member member) {
        
        String accessToken = jwtTokenProvider.createToken(
            member, 
            ExpirationTime.ACCESS_TOKEN_IN_MILLI_SECONDS
        );
        
        RefreshToken newRefreshToken = refreshTokenService
            .createAndSaveRefreshToken(member);
        
        return TokenResponse.toDto(
            accessToken, 
            newRefreshToken.getRefreshToken()
        );
    }
    
}
