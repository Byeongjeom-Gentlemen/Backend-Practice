package com.sh.global.util.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.global.exception.ErrorResponse;
import com.sh.global.exception.errorcode.ErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
        String exception = (String)request.getAttribute("exception");

        System.out.println(exception);

        if(exception.equals(UserErrorCode.NON_TOKEN.getMessage())) {
            setResponse(response, UserErrorCode.NON_TOKEN);
        }

        if(exception.equals(UserErrorCode.WRONG_TYPE_TOKEN.getMessage())) {
            setResponse(response, UserErrorCode.WRONG_TYPE_TOKEN);
        }

        if(exception.equals(UserErrorCode.WRONG_TYPE_SIGNATURE.getMessage())) {
            setResponse(response, UserErrorCode.WRONG_TYPE_SIGNATURE);
        }

        if(exception.equals(UserErrorCode.EXPIRED_ACCESS_TOKEN.getMessage())) {
            setResponse(response, UserErrorCode.EXPIRED_ACCESS_TOKEN);
        }

        if(exception.equals(UserErrorCode.MALFORMED_ACCESS_TOKEN.getMessage())) {
            setResponse(response, UserErrorCode.MALFORMED_ACCESS_TOKEN);
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
