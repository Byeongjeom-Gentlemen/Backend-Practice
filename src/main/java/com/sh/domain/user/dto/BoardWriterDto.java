package com.sh.domain.user.dto;

import com.sh.domain.user.domain.User;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardWriterDto {

    private Long id;
    private String userId;
    private String nickname;

    public BoardWriterDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
    }
}
