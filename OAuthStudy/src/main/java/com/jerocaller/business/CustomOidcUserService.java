package com.jerocaller.business;

import java.util.Map;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jerocaller.common.RoleNames;
import com.jerocaller.data.entity.Member;
import com.jerocaller.data.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OIDC 지원 provider에 대한 클래스. 
 * 리소스 서버에서 제공하는 사용자 정보를 불러오기 위한 클래스.
 * 여기서는 얻어온 사용자 정보를 DB에 저장하는 기능을 추가하기 위해 작성함. 
 * 기존에 리소스 서버로부터 사용자 정보를 가져오는 기능은 
 * OidcUserService.loadUser()를 이용함. 
 * 
 * 참고) 
 * org.springframework.security.config.oauth2.client.CommonOAuth2Provider라는 
 * 클래스에는 google, github 등 몇몇 provider들의 클라이언트 속성들이 미리 정의되어 있다. 
 * 그런데 google의 경우, builder.scope에 "openid"란 값이 포함되어 있다. 
 * 이는 google이 openid라는 프로토콜을 지원하는 것이라고 한다. 
 * openid는 OAuth를 확장하여 만든 버전이라고 한다. 
 * 그리고 OAuth는 인가가 주 목표이며, 범용 목적이지만 
 * OIDC(OpenID Connect)은 통합 인증이 주 목적이라고 하며, 즉 두 프로토콜의 목적이 다르다고 한다. 
 * 어쨌거나 Google은 OIDC를 지원하기에, CustomOAuth2UserService와 같이 
 * OAuth 기반 UserService 코드가 실행되지 않는다. 
 * 따라서 대신 다음과 같이 OIDC 기반 UserService를 만들어야 한다. 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOidcUserService extends OidcUserService {
    
    private final MemberRepository memberRepository;
    
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) 
        throws OAuth2AuthenticationException 
    {
        
        log.info("=== CustomOidcUserService.loadUser() 호출됨 ===");
        
        OidcUser oidcUser = super.loadUser(userRequest);
        
        saveOrUpdate(oidcUser);
        return oidcUser;
    }
    
    /**
     * 기존에 계정이 있을 경우 리소스 서버로부터 제공받은 유저 정보로 업데이트하고, 
     * 없을 경우 리소스 서버로부터 제공받은 유저 정보로 새로 저장.
     * 
     * @param oAuth2User  - OidcUser 인터페이스는 OAuth2User 인터페이스를 상속받기에 
     * OAuth2User 타입을 대신 사용해도 된다. 
     * @return
     */
    @Transactional
    public Member saveOrUpdate(OAuth2User oAuth2User) {
        
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        Member member = memberRepository.findByEmail(email)
            .map(entity -> entity.update(name))
            .orElse(
                Member.builder()
                    .email(email)
                    .username(name)
                    .role(RoleNames.USER)
                    .password("GoogleOAuth") // NN 회피용. 실제 사용되는 패스워드 아님.
                    .build()
            );
        return memberRepository.save(member);
    }
    
}
