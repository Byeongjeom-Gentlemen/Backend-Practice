package com.sh.global.util.jwt;

import com.sh.domain.user.service.UserRedisService;
import com.sh.global.exception.customexcpetion.TokenCustomException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRedisService userRedisService;

    public JwtVerificationFilter(JwtProvider jwtProvider, UserRedisService userRedisService) {
        this.jwtProvider = jwtProvider;
        this.userRedisService = userRedisService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtProvider.resolveAccessToken(request);

        if (accessToken != null
                && !doLogout(accessToken)
                && jwtProvider.validateToken(accessToken)) {
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean doLogout(String accessToken) {
        if (userRedisService.checkBlackListToken(accessToken)) {
            throw TokenCustomException.UNAVAILABLE_TOKENS;
        }
        return false;
    }
}
