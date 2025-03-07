package com.jerocaller.business;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jerocaller.common.CookieNames;
import com.jerocaller.data.dto.request.MemberRequest;
import com.jerocaller.data.dto.response.MemberResponse;
import com.jerocaller.data.entity.Member;
import com.jerocaller.data.repository.MemberRepository;
import com.jerocaller.exception.classes.UserAlreadyExistsException;
import com.jerocaller.jwt.JwtTokenProvider;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
    public Member getByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> 
                new EntityNotFoundException("존재하지 않는 이메일의 유저입니다.")
            );
    }
    
    @Transactional
    public MemberResponse register(MemberRequest memberRequest) {
        
        // 사용자가 회원가입을 위해 입력한 유저 네임이 이미 DB에 있는지 확인. 
        // 존재하면 가입 불가.
        Optional<Member> optMember =  memberRepository
            .findByUsername(memberRequest.getUsername());
        if (optMember.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        
        Member newMember = toEntityForPersistence(memberRequest);
        Member savedMember = memberRepository.save(newMember);
        
        return MemberResponse.toDto(savedMember);
    }
    
    /**
     * 회원 정보의 insert, update를 위해 Request DTO를 Entity로 변환시키는 메서드. 
     * 비밀번호 암호화를 위함. 
     * 
     * @param memberRequest
     * @return
     */
    private Member toEntityForPersistence(MemberRequest memberRequest) {
        return Member.builder()
            .username(memberRequest.getUsername())
            .email(memberRequest.getEmail())
            .password(passwordEncoder.encode(memberRequest.getPassword()))
            .role(memberRequest.getRole())
            .build();
    }
    
    /**
     * 요청으로 넘어온 쿠키로부터 액세스 토큰을 추출하여 이로부터 
     * 유저네임을 추출하고, DB에 해당 유저를 찾아 유저 정보를 반환한다. 
     * 
     * @param request
     * @return
     */
    public MemberResponse getMyInfoFromCookie(HttpServletRequest request) {
        
        String accessToken = jwtTokenProvider
            .resolveToken(request, CookieNames.ACCESS_TOKEN);
        String username = jwtTokenProvider.extractUsernameFromToken(accessToken);
        
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> 
                new EntityNotFoundException("해당 유저네임을 가진 유저는 없습니다.")
            );
        
        return MemberResponse.toDto(member);
    }
    
}
