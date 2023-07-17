package com.sh.global.security;

import com.sh.global.exception.customexcpetion.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/*
public class SecurityUtil {

    public static String getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("접근 권한이 존재하지 않습니다.");
        }
        return authentication.getName();
    }
}
 */
