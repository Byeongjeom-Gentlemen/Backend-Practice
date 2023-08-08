package com.sh.global.util.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.global.exception.ErrorResponse;
import com.sh.global.exception.errorcode.ErrorCode;
import com.sh.global.exception.errorcode.TokenErrorCode;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        ErrorCode exception = (ErrorCode) request.getAttribute("exception");

        // 토큰 정보가 없는 경우
        if (exception == null) {
            exception = TokenErrorCode.NON_TOKEN;
        }

        setResponse(response, exception);
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.from(errorCode)));
    }
}
