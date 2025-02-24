package com.jerocaller.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.jerocaller.common.CookieNames;
import com.jerocaller.common.ExpirationTime;
import com.jerocaller.common.RequestHeaderNames;
import com.jerocaller.common.RoleNames;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    
    // 로그인 후라면 이미 사용자 정보가 JWT에 담겨져 있으므로 
    // JWT에 있는 사용자 정보를 활용하는 것이 DB 부담을 줄일 수 있다. 
    // 즉, 한 번 인증이 되었다면 그 이후 다른 요청을 할 때 
    // 해당 사용자는 인증된 사용자라고 가정하는 방식.
    //private final UserDetailsService userDetailsService;
    private final JwtCookieUtil jwtCookieUtil;
    
    /**
     * <p>
     * io.jsonwebtoken.security.WeakKeyException: 
     * The specified key byte array is 80 bits which 
     * is not secure enough for any JWT HMAC-SHA algorithm. 
     * </p>
     * 
     * secret key가 너무 짧으면 위와 같은 예외가 발생하니 
     * secret key를 충분히 길게 작성해야 한다. 
     */
    @Value("${jwt.secret}")
    private String secretKey = "defaultKey";
    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    
    public String createToken(String username, String role) {
        
        Date expiration = new Date(
            System.currentTimeMillis() + 
            ExpirationTime.inMilliSeconds
        );
        
        String token = Jwts.builder()
            .header()
            .add("typ", CookieNames.JWT)
            .and()
            .subject(username)
            .claim("role", role)
            .issuedAt(new Date())
            .expiration(expiration)
            .signWith(getSigningKey())
            .compact();

        return token;
    }
    
    public Authentication getAuthentication(String token) {
        
        String username = extractUsernameFromToken(token);
        //UserDetails member = userDetailsService.loadUserByUsername(username);
        
        String role = (String) extractClaims(token).get("role");
        log.info("=== [getAuthentication] JWT로부터 추출한 사용자의 역할: {}", role);
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(RoleNames.ROLE_PREFIX + role));
        
        // 두 번째 인자에는 credential(자격 증명)를 넣는데, 보통은 패스워드를 넣는다. 
        // 그런데 패스워드를 얻으려면 DB로부터 조회해야 하는데, 이는 DB 접근을 최소화하는 장점을 
        // 가진 JWT의 그 장점을 활용하지 못하는 것이다. 
        // 따라서 아무 값도 넣지 않거나, 아니면 JWT 토큰 자체를 넣을 수도 있다. 
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
        
        /*
        return new UsernamePasswordAuthenticationToken(
            member.getUsername(), 
            null, 
            member.getAuthorities()
        );
        */
    }
    
    private Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith((SecretKey) getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
    
    public String extractUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }
    
    /**
     * HTTP Request header로부터 Token 값 추출.
     * 
     * Authorization: Bearer <JWT Token>
     * 형식 관련 설명 참조)
     * https://cafe.daum.net/flowlife/HqLp/36
     * 
     * @param request
     * @return
     */
    public String resolveToken(HttpServletRequest request) {
        
        String token = null;
        String tokenHeader = request.getHeader(RequestHeaderNames.AUTH_TOKEN);
        
        // 먼저 요청 헤더로부터 jwt값이 있는지 확인 후 있으면 추출하는 작업을 진행한다. 
        if (tokenHeader != null && 
            tokenHeader.startsWith(RequestHeaderNames.BEARER)
        ) {
            token = tokenHeader.substring(RequestHeaderNames.BEARER.length())
                .trim();
            log.info("Request Header로부터 JWT 추출 성공.");
        } else {
            // 요청 헤더에 없다면 요청으로 넘어온 쿠키에 jwt 값이 있는지 확인 후 추출 시도.
            token = jwtCookieUtil.extractJwtFromCookies(request);
            log.info("Request에 포함된 Cookie로부터 JWT 추출 성공");
        }
        return token;
    }
    
    /**
     * 주어진 토큰이 유효한지 체크.
     * 
     * @param token
     * @return - 아직 만료되지 않았다면 true, 만료되었거나 토큰에 문제가 있는 경우 false
     */
    public boolean validateToken(String token) {
        
        try {
            Claims claims = extractClaims(token);
            
            // 해당 토큰의 만료 시각 정보를 가져온 후, 만료 시각이 현재 시간 이전이면 true, 
            // 이후면 false가 된다. 
            // 만료 시각이 현재 시간 이전이라는 것은 이미 만료되었단 뜻이므로 
            // 더 이상 유효하지 않기에 true 값을 반전시킨 false를 반환시켜 유효하지 않음을 표시한다. 
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // 토큰 서명에 문제가 있거나 JWT 구조가 올바르지 않은 등의 이유로 예외 발생 가능.
            log.error("=== [validateToken] 토큰 유효 검사 예외 발생 ===");
            return false;
        }
        
    }
    
}
