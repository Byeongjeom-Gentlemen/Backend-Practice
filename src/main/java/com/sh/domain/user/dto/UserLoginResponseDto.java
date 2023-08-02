package com.sh.domain.user.dto;

import com.sh.domain.user.domain.User;
import java.time.LocalDateTime;

import com.sh.global.util.CustomUserDetails;
import com.sh.global.util.jwt.TokenDto;
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
    // JWT
    private TokenDto token;
    // private String result;

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
