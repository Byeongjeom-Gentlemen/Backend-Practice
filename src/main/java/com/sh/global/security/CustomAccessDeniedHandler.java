package com.sh.global.security;

import com.sh.global.common.jwt.JwtExceptionFilter;
import com.sh.global.exception.ErrorCode;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.persistence.Access;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ErrorCode errorCode;
        errorCode = ErrorCode.FORBIDDEN_REQUEST_USER;
        JwtExceptionFilter.setResponse(response, errorCode);
    }

    /*private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", errorCode.getMessage());
        responseJson.put("code", errorCode.getCode());
        responseJson.put("status", errorCode.getStatus());

        response.getWriter().print(responseJson);
    }*/
}
