package com.jerocaller.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jerocaller.entity.Members;
import com.jerocaller.repository.MembersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    
    private final MembersRepository membersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Members member = membersRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지 않습니다."));
        
        return User.builder()
            .username(member.getNickname())
            .password(member.getPassword())
            .build();
    }

}
