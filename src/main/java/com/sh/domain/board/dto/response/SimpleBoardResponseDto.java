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

    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdDate;

    public static SimpleBoardResponseDto from(Board board) {
        return SimpleBoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getUser().getNickname())
                .createdDate(board.getCreatedDate())
                .build();
    }
}
