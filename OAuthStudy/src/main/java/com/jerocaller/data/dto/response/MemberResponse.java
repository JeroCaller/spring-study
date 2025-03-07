package com.jerocaller.data.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jerocaller.data.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    
    private Integer id;
    private String username;
    private String email;
    
    @JsonIgnore
    private String password;
    
    private String role;
    private TokenResponse tokens;
    
    public static MemberResponse toDto(Member entity) {
        return MemberResponse.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .role(entity.getRole())
            .build();
    }
    
    public static MemberResponse toDtoWithToken(Member entity, TokenResponse tokens) {
        return MemberResponse.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .role(entity.getRole())
            .tokens(tokens)
            .build();
    }
    
}
