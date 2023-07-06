package com.sh.domain.user.dto;

import com.sh.domain.user.domain.Authority;
import com.sh.domain.user.domain.User;
import com.sh.global.common.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String userId;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<Authority> roles = new ArrayList<>();
    private String result;
    private String token;

    /*public UserResponseDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.createdDate = user.getCreatedDate();
        this.modifiedDate = user.getModifiedDate();
        this.roles = user.getRoles();
    }*/
}
