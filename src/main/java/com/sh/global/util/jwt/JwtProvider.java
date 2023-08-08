package com.sh.global.util.jwt;

import com.sh.domain.user.service.UserRedisService;
import com.sh.global.exception.customexcpetion.token.UnauthorizedTokenException;
import com.sh.global.exception.errorcode.TokenErrorCode;
import com.sh.global.util.CustomUserDetails;
import com.sh.global.util.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    public static final String BEARER_PREFIX = "Bearer ";

    private final CustomUserDetailsService userDetailsService;

    @Getter
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.token.access-token-expiration-millis}")
    private long accessTokenExpirationMillis;

    @Getter
    @Value("${jwt.token.refresh-token-expiration-millis}")
    private long refreshTokenExpirationMillis;

    private Key key;

    // Bean 등록 후  key secretKey HS256 decode
    @PostConstruct
    public void init() {
        String base64EncodedSecretKey = encodeBase64SecretKey(this.secretKey);
        this.key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);
    }

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 발급
    public String generateAccessToken(CustomUserDetails customUserDetails) {
        Date accessTokenExpiresIn = getTokenExpiration(accessTokenExpirationMillis);

        Claims claims = Jwts.claims().setSubject(customUserDetails.getUsername());
        claims.put("role", customUserDetails.getAuthorities());

        // Access Token -> claims(id, role) 사용자 정보, 만료시간 정보 설정
        String accessToken =
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(accessTokenExpiresIn)
                        .signWith(SignatureAlgorithm.HS256, key)
                        .compact();

        return accessToken;
    }

    // Refresh Token 발급
    public String generateRefreshToken() {
        Date refreshTokenExpiresIn = getTokenExpiration(refreshTokenExpirationMillis);

        // Refresh Token -> 만료시간 정보만 설정, 사용자 정보를 담을 필요X
        String refreshToken =
                Jwts.builder()
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(refreshTokenExpiresIn)
                        .signWith(SignatureAlgorithm.HS256, key)
                        .compact();

        return refreshToken;
    }

    // 만료 시간 설정
    private Date getTokenExpiration(long expirationMillisecond) {
        Date date = new Date();
        return new Date(date.getTime() + expirationMillisecond);
    }

    // Jwt 복호화하여 토큰 정보 반환
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("role") == null) {
            throw new RuntimeException("NO_ACCESS_ROLE");
        }

        String authority = claims.get("role").toString();
        CustomUserDetails customUserDetails = CustomUserDetails.of(claims.getSubject(), authority);

        return new UsernamePasswordAuthenticationToken(
                customUserDetails, "", customUserDetails.getAuthorities());
    }

    /*
    // 권한정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) {
    	UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
    	return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 담겨있는 유저 account 획득
    public String getAccount(String token) {
    	return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody().getSubject();
    }
    */

    // Token 복호화
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // Access Token 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new UnauthorizedTokenException(TokenErrorCode.MALFORMED_ACCESS_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedTokenException(TokenErrorCode.WRONG_TYPE_TOKEN);
        } catch (SignatureException e) {
            throw new UnauthorizedTokenException(TokenErrorCode.WRONG_TYPE_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedTokenException(TokenErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedTokenException(TokenErrorCode.NON_TOKEN);
        }
    }

    // Request Header에서 Access Token 정보를 추출하는 메서드
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Request Header에서 Refresh Token 정보를 추출하는 메서드
    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        return null;
    }
}
