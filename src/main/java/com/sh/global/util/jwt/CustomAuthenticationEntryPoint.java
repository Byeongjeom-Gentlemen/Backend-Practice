package com.sh.global.util.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.global.exception.ErrorResponse;
import com.sh.global.exception.errorcode.ErrorCode;
import com.sh.global.exception.errorcode.TokenErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorCode exception = (ErrorCode) request.getAttribute("exception");

        // 토큰 정보가 없는 경우
        if(exception == null || exception == TokenErrorCode.NON_TOKEN) {
            setResponse(response, TokenErrorCode.NON_TOKEN);
        }

        // 토큰 타입이 잘못된 경우
        if(exception == TokenErrorCode.WRONG_TYPE_TOKEN) {
            setResponse(response, TokenErrorCode.WRONG_TYPE_TOKEN);
        }

        // 토큰 시그니처가 잘못된 경우
        if(exception == TokenErrorCode.WRONG_TYPE_SIGNATURE) {
            setResponse(response, TokenErrorCode.WRONG_TYPE_SIGNATURE);
        }

        // 토큰이 만료된 경우
        if(exception == TokenErrorCode.EXPIRED_ACCESS_TOKEN) {
            setResponse(response, TokenErrorCode.EXPIRED_ACCESS_TOKEN);
        }

        // 손상된 토큰 정보일 경우
        if(exception == TokenErrorCode.MALFORMED_ACCESS_TOKEN) {
            setResponse(response, TokenErrorCode.MALFORMED_ACCESS_TOKEN);
        }

        // 블랙리스트가 된 토큰일 경우(재로그인 필요)
        if(exception == TokenErrorCode.UNAVAILABLE_TOKENS) {
            setResponse(response, TokenErrorCode.UNAVAILABLE_TOKENS);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.from(errorCode)));
    }
}
