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

import lombok.RequiredArgsConstructor;

/**
 * 리소스 서버에서 제공하는 사용자 정보를 불러오기 위한 클래스. loadUser() 메서드를 통해 
 * 리소스 서버로부터 사용자 정보를 제공받도록 구성한다. 
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // OAuth2를 이용하여 얻어온 사용자 정보에 무엇이 있는지 파악을 위한 용도. 
        // 민감 정보가 있을 수 있으니 실제 상용에서는 사용 자제.
        MapUtil.logMap(oAuth2User.getAttributes());
        
        saveOrUpdate(oAuth2User);
        return oAuth2User;
    }
    
    /**
     * 기존에 계정이 있을 경우 리소스 서버로부터 제공받은 유저 정보로 업데이트하고, 
     * 없을 경우 리소스 서버로부터 제공받은 유저 정보로 새로 저장.
     * 
     * @param oAuth2User
     * @return
     */
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
