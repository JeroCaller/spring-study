package com.jerocaller.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.data.entity.Member;
import com.jerocaller.data.entity.RefreshToken;

public interface RefreshTokenRepository 
    extends JpaRepository<RefreshToken, Integer> {
    
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    
    Optional<RefreshToken> findByMember(Member member);
    
}
