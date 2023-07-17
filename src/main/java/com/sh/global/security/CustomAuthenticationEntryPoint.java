package com.sh.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.global.exception.ErrorCode;
import com.sh.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String)request.getAttribute("exception");

        if(exception == null) {
            setResponse(response, ErrorCode.NON_TOKEN);
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(ErrorCode.WRONG_TYPE_TOKEN.getMessage())) {
            setResponse(response, ErrorCode.WRONG_TYPE_TOKEN);
        }
        //토큰 만료된 경우
        else if(exception.equals(ErrorCode.WRONG_TYPE_SIGNATURE.getMessage())) {
            setResponse(response, ErrorCode.WRONG_TYPE_SIGNATURE);
        }
        //지원하지 않는 방식의 토큰인 경우
        else if(exception.equals(ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage())) {
            setResponse(response, ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
        else {
            setResponse(response, ErrorCode.INVALID_ACCESS_TOKEN);
        }

    }

    private void setResponse(HttpServletResponse response, ErrorCode errorcode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = ErrorResponse.of(errorcode);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

 */