package com.sh.domain.user.dto.response;

import com.sh.domain.user.domain.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserBasicResponseDto {
    private Long userId;
    private String id;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static UserBasicResponseDto from(User user) {
        return UserBasicResponseDto.builder()
                .userId(user.getUserId())
                .id(user.getId())
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .build();
    }
}
