package com.sh.domain.board.dto;

import com.sh.domain.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SimpleBoardResponseDto {

    private Long boardId;
    private String title;
    private String writer;
    private LocalDateTime createDate;

    public static SimpleBoardResponseDto from(Board board) {
        return SimpleBoardResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .writer(board.getUser().getNickname())
                .createDate(board.getCreatedDate())
                .build();
    }
}
