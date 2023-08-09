package com.sh.domain.board.dto;

import com.sh.domain.board.domain.Board;
import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import com.sh.domain.user.dto.WriterResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardBasicResponseDto {

    private Long boardId;
    private String title;
    private String content;
    private WriterResponseDto user;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Integer likeCount;
    private List<SimpleCommentResponseDto> commentList;

    public static BoardBasicResponseDto from(
            Board board, List<SimpleCommentResponseDto> commentList) {
        return BoardBasicResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .user(new WriterResponseDto(board.getUser()))
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .likeCount(board.getLikeCount())
                .commentList(commentList)
                .build();
    }
}
