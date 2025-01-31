package com.jerocaller.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jerocaller.util.PermitAllRequestUriUtils;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	/**
	 * AuthenticationManager는 인증 로직의 중심 역할을 하며, 요청을 처리한다.
	 * AuthenticationConfiguration은 Spring Security의 AuthenticationManager를 
	 * 설정 및 관리하는 도우미 역할을 한다.
	 * 
	 * @param authConfig
	 * @return
	 * @throws Exception 
	 */
	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration authConfig
	) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	/**
	 * REST API 제작 및 세션 인증 기반을 위한 시큐리티 설정.
	 * 
	 * @param httpSecurity
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity httpSecurity
	) throws Exception {
		
		httpSecurity
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(PermitAllRequestUriUtils.getPermitAllRequestUris())
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
			//.exceptionHandling(exception -> new Exception("security 인증 과정에서 무언가 잘못되었습니다."));
		
		return httpSecurity.build();
	} 
	
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080/", "http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		
		// 클라이언트로부터 인증 정보(credential) 전송 시
		// 이를 받도록 허용할 것인지 여부 설정. 
		// 여기 백엔드에서와 프론트엔드 둘 모두 true로 설정해야 둘 사이에서 인증 정보를 교환할 수 있다. 
		configuration.setAllowCredentials(true); 
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	/**
	 * 사용자 패스워드 보호를 위한 패스워드 암호화 기능.
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * 인증 정보를 세션에 저장하기 위한 Bean.
	 * HttpSessionSecurityContextRepository은 
	 * SecurityContextRepository의 구현체이며, SecurityContext 객체 생성 후 
	 * 이 객체를 세션에 저장하는 역할을 한다. 또한 세션에 저장된 해당 객체를 
	 * 조회, 참조하기도 한다. 
	 * 
	 * 참고 사이트)
	 * https://gngsn.tistory.com/160
	 * 
	 * @return
	 */
	@Bean
	public SecurityContextRepository securityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}
	
	@Bean
	public SecurityContextLogoutHandler getLogoutHandler() {
		return new SecurityContextLogoutHandler();
	}

}
