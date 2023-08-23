package com.sh.domain.comment.dto;

import com.sh.domain.comment.domain.Comment;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.WriterDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SimpleCommentResponseDto {

    private Long commentId;
    private String content;
    private WriterDto writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedData;

    public SimpleCommentResponseDto(Long commentId, String content, User user, LocalDateTime createdDate, LocalDateTime modifiedData) {
        this.commentId = commentId;
        this.content = content;
        this.writer = new WriterDto(user);
        this.createdDate = createdDate;
        this.modifiedData = modifiedData;
    }

    public static SimpleCommentResponseDto of(Comment comment, User user) {
        return SimpleCommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .writer(new WriterDto(user))
                .createdDate(comment.getCreatedDate())
                .modifiedData(comment.getModifiedDate())
                .build();
    }
}
