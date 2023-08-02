package com.sh.global.util.jwt;

import com.sh.global.exception.customexcpetion.token.UnauthorizedTokenException;
import com.sh.global.exception.errorcode.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UnauthorizedTokenException e) {
            ErrorCode errorCode = e.getErrorCode();
            request.setAttribute("exception", errorCode);
            filterChain.doFilter(request, response);
        }

    }
}
