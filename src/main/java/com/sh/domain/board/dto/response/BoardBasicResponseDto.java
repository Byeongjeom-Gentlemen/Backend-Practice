package com.sh.domain.board.dto.response;

import com.sh.domain.board.domain.Board;
import com.sh.domain.user.dto.WriterDto;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardBasicResponseDto {

    private Long boardId;
    private String title;
    private String content;
    private WriterDto writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Integer viewCount;
    private Integer likeCount;

    public static BoardBasicResponseDto from(Board board) {
        return BoardBasicResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(new WriterDto(board.getUser()))
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .build();
    }
}
