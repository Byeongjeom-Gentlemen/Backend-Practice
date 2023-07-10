package com.sh.global.security;

import com.sh.global.common.jwt.JwtAuthenticationFilter;
import com.sh.global.common.jwt.JwtExceptionFilter;
import com.sh.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
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
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 1. 토큰 없음
        /*if(authorization == null) {
            log.info("토큰이 존재하지 않습니다.");
            JwtExceptionFilter.setResponse(response, ErrorCode.NON_TOKEN);
        }
        
        // 2. 시그니처 불일치
        else if (!authorization.startsWith("Bearer ")) {
            log.info("토큰 시그니처가 일치하지 않습니다.");
            JwtExceptionFilter.setResponse(response, ErrorCode.INVALID_TOKEN);
        }

        else if (authorization.equals(ErrorCode.EXPIRED_TOKEN)) {
            log.info("토큰이 만료되었습니다.");
            JwtExceptionFilter.setResponse(response, ErrorCode.EXPIRED_TOKEN);
        }*/


    }

    /*private void setResponse(HttpServletResponse response, ErrorCode errorcode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("code", errorcode.getCode());
        responseJson.put("status", errorcode.getStatus());
        responseJson.put("message", errorcode.getMessage());

        response.getWriter().println(responseJson);
    }*/
}
