package com.sh.domain.user.dto;

import com.sh.global.util.CustomUserDetails;
import com.sh.global.util.jwt.TokenDto;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto {

    private Long userId;
    private String id;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private TokenDto token;

    public static UserLoginResponseDto from(CustomUserDetails user, TokenDto token) {
        return UserLoginResponseDto.builder()
                .userId(user.getUserId())
                .id(user.getId())
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .token(token)
                .build();
    }
}
