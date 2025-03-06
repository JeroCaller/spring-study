package com.jerocaller.config.oauth;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.jerocaller.common.CookieNames;
import com.jerocaller.common.ExpirationTime;
import com.jerocaller.data.dto.request.CookieRequest;
import com.jerocaller.util.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * OAuth2에 대해 인증 요청 관련 상태를 쿠키에 저장, 불러오기 기능을 위한 클래스.
 * 
 * AuthorizationRequestRepository는 권한 인증 흐름에서 클라이언트의 요청을 유지하기 위해 
 * 사용되는 요청 저장소 역할을 한다. 
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthorizationRequestBasedOnCookieRepository 
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    
    private final CookieUtil cookieUtil;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(
        HttpServletRequest request
    ) {
        
        Cookie cookie = WebUtils.getCookie(
            request, 
            CookieNames.OAUTH2_AUTHORIZATION_REQUEST
        );
        
        return cookieUtil.deserialize(
            cookie, 
            OAuth2AuthorizationRequest.class
        );
    }

    @Override
    public void saveAuthorizationRequest(
        OAuth2AuthorizationRequest authorizationRequest, 
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        
        // 권한(인증) 요청이 없다면 기존 저장소에 저장된 인증 요청 관련 상태 삭제 및 종료.
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(response);
            return;
        }
        
        // 인증 권한 요청 관련 상태를 쿠키에 실어 응답하도록 구성.
        CookieRequest cookieRequest = CookieRequest.builder()
            .cookieName(CookieNames.OAUTH2_AUTHORIZATION_REQUEST)
            .cookieValue(cookieUtil.serialize(authorizationRequest))
            .maxAge(ExpirationTime.OAUTH2_AUTH_REQUEST_IN_SECONDS)
            .build();
        cookieUtil.addCookie(response, cookieRequest);
        
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        return loadAuthorizationRequest(request);
    }
    
    /**
     * 보안을 위해 권한 요청 관련 상태 정보들이 담긴 쿠키를 삭제한다. 
     * 
     * @param response
     */
    public void removeAuthorizationRequestCookies(
        HttpServletResponse response
    ) {
        
        cookieUtil.deleteCookies(
            response, 
            CookieNames.OAUTH2_AUTHORIZATION_REQUEST
        );
        
    }

}
