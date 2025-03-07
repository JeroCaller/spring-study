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
    
    /**
     * 참고 자료를 따라 구현함. 이 메서드에서 단순히 loadAuthorizationRequest() 메서드의 반환값을 
     * 그대로 반환하도록 한 이유는 권한 요청을 위한 상태 정보들을 쿠키에 담아 임시로 저장하는 방식을 채택할 경우, 
     * 이를 삭제하기 위해 별도의 메서드를 만들어 해당 메서드를 사용할 의도로 추정됨. 
     * 쿠키에 담긴 요청 정보를 삭제한다는 것을 부각 시키기 위한 의미도 있을 것으로 추측됨. 
     * 따라서 이 경우 별도의 메서드를 만들었기에 이 메서드를 오버라이딩할 필요가 없어 
     * 임시로 이러한 코드를 둔 것으로 추측됨. 
     * 
     */
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
