package com.sh.global.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.global.exception.ErrorCode;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    /*
    인증 오류가 아닌, JWT 관련 오류는 이 필터에서 따로 잡아낸다.
    이를 통해 JWT 만료 에러와 인증 에러를 따로 잡아낼 수 있다.
     */

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (JwtException e) {
            String authorizationValue = request.getHeader("Authorization");
            System.out.println(authorizationValue);
            String message = e.getMessage();

            if(authorizationValue == null || authorizationValue.equals("")) {
                setResponse(response, ErrorCode.UNKNOWN_ERROR);
            }
            // 토큰이 만료된 경우
            else if(ErrorCode.EXPIRED_TOKEN.getMessage().equals(message)) {
                setResponse(response, ErrorCode.EXPIRED_TOKEN);
            }
            // 잘못된 타입의 토큰인 경우
            else if (ErrorCode.WRONG_TYPE_TOKEN.getMessage().equals(message)) {
                setResponse(response, ErrorCode.WRONG_TYPE_TOKEN);
            }
            // 지원되지 않은 토큰인 경우
            else if (ErrorCode.UNSUPPORTED_TOKEN.getMessage().equals(message)) {
                setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
            }
            else if (ErrorCode.UNKNOWN_ERROR.getMessage().equals(message)) {
                setResponse(response, ErrorCode.UNKNOWN_ERROR);
            }
            else {
                setResponse(response, ErrorCode.ACCESS_DENIED);
            }
        }
    }

    public static void setResponse(HttpServletResponse response, ErrorCode errorCode) {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());

        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class ErrorResponse {
        private final HttpStatus status;
        private final String code;
        private final String message;
    }
}
