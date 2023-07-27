package com.sh.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LikeResponseDto {

    private Long likeId;
    private String status;

    public static LikeResponseDto of(Long likeId, String status) {
        return LikeResponseDto.builder()
                .likeId(likeId)
                .status(status)
                .build();
    }
}
