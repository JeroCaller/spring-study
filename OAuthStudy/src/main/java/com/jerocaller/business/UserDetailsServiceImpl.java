package com.jerocaller.business;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jerocaller.data.entity.Member;
import com.jerocaller.data.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final MemberRepository memberRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws 
        UsernameNotFoundException 
    {
        
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("해당 유저는 존재하지 않습니다.")
            );
        
        return member;
    }

}
