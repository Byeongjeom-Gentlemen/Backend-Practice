package com.sh.domain.board.dto;

import com.sh.domain.board.domain.Board;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.BoardWriterDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardBasicResponseDto {

    private Long boardId;
    private String title;
    private String content;
    private BoardWriterDto user;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static BoardBasicResponseDto from(Board board) {
        return BoardBasicResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .user(new BoardWriterDto(board.getUser()))
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }
}
