package com.jerocaller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jerocaller.common.CookieNames;
import com.jerocaller.common.RoleNames;
import com.jerocaller.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final String[] permitAllRequestUris = {
        "/swagger-ui/**", 
        "/v3/api-docs",
        "/v3/api-docs/**", 
        "/swagger-ui.html",
        "/swagger-ui/index.html",
        "/auth/**",
        "/members/registration",
    };
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) 
        throws Exception 
    {
        
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(manager -> manager
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(permitAllRequestUris).permitAll()
                /*
                .requestMatchers("/comments/others/users").hasAnyRole(
                    RoleNames.USER, RoleNames.STAFF, RoleNames.ADMIN
                )
                .requestMatchers("/comments/others/staffs").hasAnyRole(
                    RoleNames.STAFF, RoleNames.ADMIN
                )
                .requestMatchers("/comments/others/admins").hasAnyRole(
                    RoleNames.ADMIN
                ) */
                .requestMatchers("/comments/others/users").hasRole(RoleNames.USER)
                .requestMatchers("/comments/others/staffs").hasRole(RoleNames.STAFF)
                .requestMatchers("/comments/others/admins").hasRole(RoleNames.ADMIN)
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .deleteCookies(CookieNames.JWT)
                .clearAuthentication(true)
                .permitAll()
            )
            .addFilterBefore(
                jwtAuthenticationFilter, 
                UsernamePasswordAuthenticationFilter.class
            )
            .exceptionHandling(handle -> handle
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
            );
        
        return httpSecurity.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    /**
     * Role 계층 구성. 
     * 
     * @return
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role(RoleNames.ADMIN).implies(RoleNames.STAFF)
            .role(RoleNames.STAFF).implies(RoleNames.USER)
            .build();
    }
    
}
