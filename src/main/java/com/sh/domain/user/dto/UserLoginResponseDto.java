package com.sh.domain.user.dto;

import com.sh.domain.user.domain.Authority;
import com.sh.domain.user.domain.User;
import com.sh.global.common.SessionDto;
// import com.sh.global.common.jwt.TokenDto;
import lombok.*;
import org.springframework.boot.web.server.Cookie;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto {

    private Long id;
    private String userId;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<Authority> roles = new ArrayList<>();
    // JWT
    //private TokenDto token;

    // 세션
    private SessionDto session;

    // JWT
    /*public static UserLoginResponseDto from(User user, TokenDto token) {
        return UserLoginResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .roles(user.getRoles())
                .token(token)
                .build();
    }*/

    // Session
    public static UserLoginResponseDto from(User user, SessionDto session) {
        return UserLoginResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .roles(user.getRoles())
                .session(session)
                .build();
    }
}
