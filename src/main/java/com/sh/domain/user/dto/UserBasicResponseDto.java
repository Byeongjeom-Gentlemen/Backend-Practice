package com.sh.domain.user.dto;

import com.sh.domain.user.domain.Authority;
import com.sh.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicResponseDto {
    private Long id;
    private String userId;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    public static UserBasicResponseDto from(User user) {
        return UserBasicResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .build();
    }
}