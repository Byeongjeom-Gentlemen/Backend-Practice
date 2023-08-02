package com.sh.global.util;

import com.sh.domain.user.util.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class CustomAuthorityUtils {

    // 입력된 role 값을 기반으로 권한 정보를 생성하여 List<GrantedAuthority> 타입으로 변환한다.
    // 권한 정보는 “ROLE_USER”, “ROLE_ADMIN” 형식으로 생성된다.
    public static List<GrantedAuthority> createAuthority(String role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // 입력된 role 값이 유효한 권한인지 검증한다.
    public static void verifiedRole(String role) {
        if(role == null) {
            throw new RuntimeException("USER_ROLE_DOES_NOT_EXIST");
        }

        if(!role.equals("ROLE_USER") && !role.equals("ROLE_ADMIN")) {
            throw new RuntimeException("USER_ROLE_INVALID");
        }
    }
}
