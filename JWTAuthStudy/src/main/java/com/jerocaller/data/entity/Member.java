package com.jerocaller.data.entity;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.jerocaller.data.dto.request.MemberRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 11)
    private int id;
    
    @Column(nullable = false, length = 20, unique = true)
    private String username;
    
    @Column(nullable = false, length = 100)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    
    @Column(length = 10, name = "role_name")
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        final String prefix = "ROLE_";
        GrantedAuthority auth = new SimpleGrantedAuthority(
            prefix + this.role
        );
                
        return Arrays.asList(auth);
    }
    
    public static Member toEntityForInserting(MemberRequest dto) {
        return Member.builder()
            .username(dto.getUsername())
            .password(dto.getPassword())
            .role(dto.getRole())
            .build();
    }
    
}
