package com.sh.global.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken(request);

        try {
            if(StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // 에러가 발생했을 때, request에 attribute를 세팅하고 RestAuthenticationEntryPoint로 request를 넘겨준다.
        } catch (SignatureException e) {
            log.info("잘못된 JWT 서명입니다.");
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_SIGNATURE.getMessage());
        } catch (MalformedJwtException e) {
            log.info("유효하지 않은 구성의 JWT 토큰입니다.");
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            request.setAttribute("exception", ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 형식이나 구성의 JWT 토큰입니다.");
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            log.info(e.toString().split(":")[1].trim());
            request.setAttribute("exception", ErrorCode.INVALID_ACCESS_TOKEN.getMessage());
        }

        chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
