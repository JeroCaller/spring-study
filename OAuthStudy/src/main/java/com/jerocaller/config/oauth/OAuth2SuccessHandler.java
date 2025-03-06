package com.jerocaller.config.oauth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.jerocaller.business.MemberService;
import com.jerocaller.business.RefreshTokenService;
import com.jerocaller.common.CookieNames;
import com.jerocaller.common.ExpirationTime;
import com.jerocaller.data.dto.request.CookieRequest;
import com.jerocaller.data.entity.Member;
import com.jerocaller.data.entity.RefreshToken;
import com.jerocaller.jwt.JwtTokenProvider;
import com.jerocaller.util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler 
    extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository 
        authorizationRequestRepository;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;
    
    private final String REDIRECT_URI = "/oauth/login/success";
    
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, 
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        
        // 리프레시 토큰 DB 저장을 위해 DB로부터 현재 유저 정보 추출.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String emailByOAuth = oAuth2User.getAttribute("email");
        Member member = memberService.getByEmail(emailByOAuth);
        
        // 리프레시 토큰 발급 및 쿠키에 담아 전송
        RefreshToken refreshToken = refreshTokenService
            .createAndSaveRefreshToken(member);
        addRefreshTokenToCookie(
            request, 
            response, 
            refreshToken.getRefreshToken()
        );
        
        // 액세스 토큰 발급 및 리다이렉트 경로 뒤에 쿼리 스트링으로 해당 토큰 추가.
        // 발급된 액세스 토큰을 리다이렉트된 컨트롤러 메서드에서 유저로 응답하기 위함. 
        String accessToken = jwtTokenProvider.createToken(
            member, 
            ExpirationTime.ACCESS_TOKEN_IN_MILLI_SECONDS
        );
        String redirectUri = getRedirectUriWithTokenAsParams(accessToken);
        
        // 인증을 위해 생성, 저장된 관련 설정값들을 보안을 위해 쿠키, 세션에서 제거.
        clearAuthenticationStates(request, response);
        
        // 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
        
    }
    
    private void addRefreshTokenToCookie(
        HttpServletRequest request, 
        HttpServletResponse response,
        String refreshToken
    ) {
        
        // 기존에 리프레시 토큰이 있다면 먼저 이를 삭제
        cookieUtil.deleteCookies(response, CookieNames.REFRESH_TOKEN);
        
        // 대상 리프레시 토큰을 쿠키에 저장. 
        CookieRequest cookieRequest = CookieRequest.builder()
            .maxAge(ExpirationTime.REFRESH_TOKEN_IN_SECONDS)
            .cookieName(CookieNames.REFRESH_TOKEN)
            .cookieValue(refreshToken)
            .build();
        cookieUtil.addCookie(response, cookieRequest);
        
    }
    
    /**
     * 토큰을 리다이렉트 대상 URI 뒤에 쿼리 스트링으로 추가한다. 
     * 
     * @param token
     * @return
     */
    private String getRedirectUriWithTokenAsParams(String token) {
        
        return UriComponentsBuilder.fromUriString(REDIRECT_URI)
            .queryParam("token", token)  // .../some-uri?token=access_token 형태
            .build()
            .toUriString();
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
