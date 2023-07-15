package com.sh.domain.user.dto;

import com.sh.domain.user.domain.Authority;
import com.sh.domain.user.domain.User;
import com.sh.global.common.jwt.TokenDto;
import lombok.*;

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
    private TokenDto token;

    public static UserLoginResponseDto of(User user, TokenDto token) {
        return UserLoginResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .roles(user.getRoles())
                .token(token)
                .build();
    }
}
