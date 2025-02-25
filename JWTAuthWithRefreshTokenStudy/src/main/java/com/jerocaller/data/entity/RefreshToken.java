package com.jerocaller.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tb_refresh_token")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class RefreshToken extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(length = 1000, unique = true)
    private String refreshToken;
    
    @Column(length = 1000, unique = true)
    private String beforeToken;
    
    /**
     * 참고 사이트)
     * https://velog.io/@yarogono/JPA-MariaDB-BOOLEAN%EC%BB%AC%EB%9F%BC%EA%B3%BC-Enum
     * 
     * 자바 Enum에 명시된 데이터들을 DB에 저장하도록 함. 
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "is_invalid", length = 20, nullable = false)
    @Builder.Default
    private RefreshTokenStatus tokenStatus = RefreshTokenStatus.VALID;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;
    
}
