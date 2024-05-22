package com.ssafy.urturn.global.auth;


import com.ssafy.urturn.global.auth.repository.JwtRedisRepository;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import com.ssafy.urturn.global.util.AES128Util;
import com.ssafy.urturn.global.util.KeyUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtTokenProvider {

    private JwtRedisRepository jwtRedisRepository;
    private Key key;
    private long accessTokenValidityInSeconds;
    private long refreshTokenValidityInSeconds;
    private final AES128Util aes128Util;

    public static final String HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";

    // application.yml에서 secret 값 가져와서 key에 저장
    @Autowired
    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey,
                            @Value("${spring.jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
                            @Value("${spring.jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds,
                            JwtRedisRepository jwtRedisRepository,
                            AES128Util aes128Util) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // base64로 디코딩 -> 바이트 배열로 변환
        this.key = Keys.hmacShaKeyFor(keyBytes); // hmacsha256으로 다시 암호화?
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
        this.jwtRedisRepository = jwtRedisRepository;
        this.aes128Util = aes128Util;
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtToken generateToken(Authentication authentication) {

        Claims claims = Jwts.claims().setSubject(authentication.getName());
        // 권한 가져오기
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        claims.put("roles", roles);

        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claims)
                .setExpiration(new Date(now + accessTokenValidityInSeconds * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claims)
                .setExpiration(new Date(now + refreshTokenValidityInSeconds * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String encryptedRefreshToken = aes128Util.encryptAes(refreshToken);

        // refreshToken redis에 저장
       jwtRedisRepository.save(
               KeyUtil.getRefreshTokenKey(authentication.getName()),
               refreshToken, refreshTokenValidityInSeconds);

        return JwtToken.builder()
                .grantType(TOKEN_PREFIX)
                .accessToken(accessToken)
                .refreshToken(encryptedRefreshToken)
                .build();
    }




    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화 (Claim이 권한 정보)
        Claims claims = parseClaims(accessToken);

        if (claims.get("roles") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기 (권한 정보가 Set<String>으로 저장되어 있다고 가정)
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");

        Collection<? extends GrantedAuthority> authorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        // User : username, password,authorities
        // -> UserDetails은 사용자 식별과 권한만 확인, 보안을 위해 SS에서 password 비워두는 것이 관행
        // UsernamePasswordAuthenticationToken : Authentication 구현체
        // credentials : 패스워드나 토큰과 같은 비밀번호 인증 정보 ( 보안을 위해 비워둠 )
        // credentials이 없어도 principal, authorities정보에 중점을 두고 처리
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String accessToken) {
        try {
            log.debug("accessToken : {}", accessToken);
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new JwtException("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            // access token이 expire될 시에 바로 reissue로 redirect
            log.info("Expired JWT Token", e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new JwtException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new JwtException("JWT claims string is empty.");
        }
    }

    // accessToken claim parsing (만료된 토큰도 복호화)
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) // JWT 토큰 검증과 파싱 모두 수행
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Request Header에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length()+1);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")){
                return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    public String generateNewAccessToken(String refreshToken){

        Authentication authentication = this.getAuthentication(refreshToken);
        String memberId = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Date accessTokenExpiresIn = new Date((new Date()).getTime() + accessTokenValidityInSeconds * 1000);
        return Jwts.builder()
                .setSubject(memberId)
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean existsRefreshToken(String refreshToken) {
        // userId 정보를 가져와서 redis에 있는 refreshtoken과 같은지 확인
        Claims claims = this.parseClaims(refreshToken);
        String memberId = claims.getSubject();
        return refreshToken.equals(jwtRedisRepository.find(KeyUtil.getRefreshTokenKey(memberId)).orElse(""));
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("authorization", "bearer "+ accessToken);
    }
}
