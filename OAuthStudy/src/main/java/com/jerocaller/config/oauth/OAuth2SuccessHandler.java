package com.jerocaller.config.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.jerocaller.business.CustomOAuth2UserService;
import com.jerocaller.business.MemberService;
import com.jerocaller.business.RefreshTokenService;
import com.jerocaller.common.CookieNames;
import com.jerocaller.common.ExpirationTime;
import com.jerocaller.data.dto.request.CookieRequest;
import com.jerocaller.data.dto.request.MemberRequest;
import com.jerocaller.data.entity.Member;
import com.jerocaller.data.entity.RefreshToken;
import com.jerocaller.data.repository.MemberRepository;
import com.jerocaller.jwt.JwtTokenProvider;
import com.jerocaller.util.CookieUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler 
    extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository 
        authorizationRequestRepository;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
    
    //private final String REDIRECT_URI = "/oauth2/login/success";
    @Value("${frontend.url.oauth2.login.success}")
    private String REDIRECT_URI;
    
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, 
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        
        // 리프레시 토큰 DB 저장을 위해 DB로부터 현재 유저 정보 추출.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String emailByOAuth = oAuth2User.getAttribute("email");
        
        Member member = null;
        try {
            member = memberService.getByEmail(emailByOAuth);
        } catch (EntityNotFoundException e) {
            // 해당 유저가 DB에 존재하지 않을 경우 DB에 등록한다. 
            member = customOAuth2UserService.saveOrUpdate(oAuth2User);
        }
        
        // 액세스 토큰 발급
        String accessToken = jwtTokenProvider.createToken(
            member, 
            ExpirationTime.ACCESS_TOKEN_IN_MILLI_SECONDS
        );
        
        // 리프레시 토큰 발급
        RefreshToken refreshToken = refreshTokenService
            .createAndSaveRefreshToken(member);
        
        // 두 토큰 모두 쿠키에 전송
        CookieRequest accessTokenRequest = CookieRequest.builder()
            .cookieName(CookieNames.ACCESS_TOKEN)
            .cookieValue(accessToken)
            .maxAge(ExpirationTime.ACCESS_TOKEN_IN_SECONDS)
            .build();
        CookieRequest refreshTokenRequest = CookieRequest.builder()
            .cookieName(CookieNames.REFRESH_TOKEN)
            .cookieValue(refreshToken.getRefreshToken())
            .maxAge(ExpirationTime.REFRESH_TOKEN_IN_SECONDS)
            .build();
        addTokenToCookie(
            request, 
            response, 
            accessTokenRequest, 
            refreshTokenRequest
        );
        
        // 인증을 위해 생성, 저장된 관련 설정값들을 보안을 위해 쿠키, 세션에서 제거.
        clearAuthenticationStates(request, response);
        
        getRedirectStrategy().sendRedirect(request, response, REDIRECT_URI);
        
    }
    
    private void addTokenToCookie(
        HttpServletRequest request, 
        HttpServletResponse response,
        CookieRequest ...cookieRequests
    ) {
        
        for (CookieRequest cookieRequest : cookieRequests) {
            // 기존에 해당 토큰이 쿠키에 있다면 이를 먼저 삭제
            cookieUtil.deleteCookies(response, cookieRequest.getCookieName());
            
            // 새 토큰을 쿠키에 담아 응답에 싣는다.
            cookieUtil.addCookie(response, cookieRequest);
        }
        
    }
    
    /**
     * 인증 과정에서 임시로 생성, 저장된 인증 정보를 삭제. 
     * 쿠키 및 세션에 있을 수 있는 인증 정보 삭제.
     * 
     * @param request
     * @param response
     */
    private void clearAuthenticationStates(
        HttpServletRequest request, 
        HttpServletResponse response
    ) {
        
        // 인증 과정에서 세션에 저장되어 있을 수 있는 인증 관련 정보 삭제.
        super.clearAuthenticationAttributes(request);
        
        // 쿠키에 존재하는 인증 관련 정보 삭제.
        authorizationRequestRepository
            .removeAuthorizationRequestCookies(response);
        
    }
    
}
