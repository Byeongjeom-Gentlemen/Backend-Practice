package com.sh.domain.comment.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommentListResponseDto {

    private boolean hasNext;
    private List<SimpleCommentResponseDto> commentList;

    public static CommentListResponseDto of(
            boolean hasNext, List<SimpleCommentResponseDto> commentList) {
        return CommentListResponseDto.builder().hasNext(hasNext).commentList(commentList).build();
    }
}
