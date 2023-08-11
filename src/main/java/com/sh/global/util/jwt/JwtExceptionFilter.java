package com.sh.global.util.jwt;

import com.sh.global.exception.customexcpetion.TokenCustomException;
import com.sh.global.exception.errorcode.ErrorCode;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenCustomException e) {
            ErrorCode errorCode = e.getErrorCode();
            request.setAttribute("exception", errorCode);
            filterChain.doFilter(request, response);
        }
    }
}
