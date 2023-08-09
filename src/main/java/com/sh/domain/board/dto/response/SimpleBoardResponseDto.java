package com.sh.domain.board.dto.response;

import com.sh.domain.board.domain.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SimpleBoardResponseDto {

    private LocalDateTime createdDate;
    private Long id;
    private String title;
    private String writer;

    public static SimpleBoardResponseDto from(Board board) {
        return SimpleBoardResponseDto.builder()
                .createdDate(board.getCreatedDate())
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getUser().getNickname())
                .build();
    }
}
