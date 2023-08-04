package com.sh.global.util;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.customexcpetion.token.UnauthorizedTokenException;
import com.sh.global.exception.customexcpetion.user.UnauthorizedUserException;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    // 현재 시큐리티 홀더에 저장되어 있는 유저인증정보 가져오기
    public String getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if(authentication == null || authentication.getName() == null) {
            throw new UnauthorizedUserException(UserErrorCode.FORBIDDEN_REQUEST_USER);
        }

        return authentication.getName();
    }
}
