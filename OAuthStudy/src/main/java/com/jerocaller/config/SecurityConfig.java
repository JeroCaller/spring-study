package com.jerocaller.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.jerocaller.business.CustomOAuth2UserService;
import com.jerocaller.config.filter.CorsLoggingFilter;
import com.jerocaller.config.oauth.OAuth2FailureHandler;
import com.jerocaller.config.oauth.OAuth2SuccessHandler;
import com.jerocaller.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest>
        authorizationRequestRepository;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    
    private final String[] permitAllRequestUris = {
        "/swagger-ui/**", 
        "/v3/api-docs",
        "/v3/api-docs/**", 
        "/swagger-ui.html",
        "/swagger-ui/index.html",
        "/auth/**",
        "/members/registration",
        "/jwt/tokens/**",
        "/oauth2/**"
    };
    
    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity httpSecurity
    ) throws Exception {
        
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(formLogin -> formLogin.disable())
            .logout(logout -> logout.disable())
            .sessionManagement(manager -> manager
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(permitAllRequestUris).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new CorsLoggingFilter(), CorsFilter.class)
            .addFilterBefore(
                jwtAuthenticationFilter, 
                UsernamePasswordAuthenticationFilter.class
            )
            .oauth2Login(login -> login
                .authorizationEndpoint(endPoint -> endPoint
                    .authorizationRequestRepository(
                        authorizationRequestRepository
                    )
                )
                .userInfoEndpoint(endPoint -> endPoint
                    .userService(customOAuth2UserService)
                )
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(new OAuth2FailureHandler())
            )
            .exceptionHandling(handler -> handler
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            );
        
        return httpSecurity.build();
    }
    
    /**
     * Spring Bean 간의 순환 참조 방지를 위해 static으로 선언함. 
     * 
     * 이 설정 클래스에서 의존성으로 주입받은 MemberService 내부에 
     * PasswordEncoder를 의존성 주입받는 코드가 있기에 순환 참조에 걸림.
     * 
     * @return
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }
    
    public CorsConfigurationSource corsConfigurationSource() {
        
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            //"http://localhost:8080", 
            "http://localhost:3000"
        ));
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        //configuration.setAllowedHeaders(Arrays.asList(
        //    "Access-Control-Allow-Origin"
        //));
        
        // 클라이언트로부터 인증 정보(credential) 전송 시
        // 이를 받도록 허용할 것인지 여부 설정. 
        // 여기 백엔드에서와 프론트엔드 둘 모두 true로 설정해야 둘 사이에서 인증 정보를 교환할 수 있다. 
        configuration.setAllowCredentials(true); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
}
