package com.jerocaller.business;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jerocaller.common.RoleNames;
import com.jerocaller.data.entity.Member;
import com.jerocaller.data.repository.MemberRepository;
import com.jerocaller.util.MapUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 리소스 서버에서 제공하는 사용자 정보를 불러오기 위한 클래스. loadUser() 메서드를 통해 
 * 리소스 서버로부터 사용자 정보를 제공받도록 구성한다. 
 * 
 * 리소스 서버로부터 사용자 정보를 얻어오는 기능은 DefaultOAuth2UserService에 이미 구현되어 있다. 
 * 여기서는 해당 사용자 정보를 DB에 저장하기 위한 기능을 추가하기 위해 해당 클래스를 상속받아 
 * 오버라이딩하였다. 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) 
        throws OAuth2AuthenticationException 
    {
        
        log.info("=== CustomOAuth2UserService.loadUser() 호출됨 ===");
        
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // OAuth2를 이용하여 얻어온 사용자 정보에 무엇이 있는지 파악을 위한 용도. 
        // 민감 정보가 있을 수 있으니 실제 상용에서는 주의하여 사용해야 함.
        MapUtil.logMap(oAuth2User.getAttributes());
        
        saveOrUpdate(oAuth2User);
        return oAuth2User;
    }
    
    /**
     * 기존에 계정이 있을 경우 리소스 서버로부터 제공받은 유저 정보로 업데이트하고, 
     * 없을 경우 리소스 서버로부터 제공받은 유저 정보로 새로 저장.
     * 
     * Issue)
     * 여기서 이 메서드를 호출하면 구글로부터 얻어온 사용자 정보가 DB에 저장되지 않는다. 
     * 그래서 successHandler에서 단순히 findByEmail() 등을 통해 DB로부터 
     * 사용자 정보 조회 시도 시 에러가 발생한다. 
     * 현재는 이 메서드를 successHandler에서도 호출하도록 하여 해결하였다.
     * 
     * @param oAuth2User
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
