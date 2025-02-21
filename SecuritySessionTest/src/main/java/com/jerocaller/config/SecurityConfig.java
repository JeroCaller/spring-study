package com.jerocaller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final String[] permitAllUris = {
        "/swagger-ui/**", 
        "/v3/api-docs",
        "/v3/api-docs/**", 
        "/swagger-ui.html",
        "/swagger-ui/index.html",
        "/auth/**", 
        "/members",
        "/members/**",
        "/files/**",
        "/test/**",
    };
    
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity httpSecurity
    ) throws Exception {
        
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(permitAllUris)
                .permitAll()  // 지정된 URL에 대한 접근은 모든 이들에게 허용.
                .anyRequest().authenticated() // 그 외 요청은 인증 필요.
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)  // 세션 무효화
                .clearAuthentication(true)  // 인증 정보 무효화
                .permitAll()
            );
        
        return httpSecurity.build();
    }

}
