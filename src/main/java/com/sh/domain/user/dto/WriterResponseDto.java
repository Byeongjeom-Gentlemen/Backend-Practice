package com.sh.domain.user.dto;

import com.sh.domain.user.domain.User;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WriterResponseDto {

    private Long userId;
    private String id;
    private String nickname;

    public WriterResponseDto(User user) {
        this.userId = user.getUserId();
        this.id = user.getId();
        this.nickname = user.getNickname();
    }
}
