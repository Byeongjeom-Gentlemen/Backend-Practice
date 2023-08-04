package com.sh.global.util.jwt;

import com.sh.domain.user.service.BlackListTokenService;
import com.sh.global.exception.customexcpetion.token.UnauthorizedTokenException;
import com.sh.global.exception.errorcode.TokenErrorCode;
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
    private final BlackListTokenService blackListTokenService;

    public JwtVerificationFilter(
            JwtProvider jwtProvider, BlackListTokenService blackListTokenService) {
        this.jwtProvider = jwtProvider;
        this.blackListTokenService = blackListTokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtProvider.resolveAccessToken(request);

        if (accessToken != null
                && jwtProvider.validateToken(accessToken)
                && !doLogout(accessToken)) {
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean doLogout(String accessToken) {
        if (blackListTokenService.checkBlackListToken(accessToken)) {
            throw new UnauthorizedTokenException(TokenErrorCode.UNAVAILABLE_TOKENS);
        }
        return false;
    }
}
