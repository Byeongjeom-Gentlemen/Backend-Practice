package com.sh.domain.comment.dto;

import com.sh.domain.comment.domain.Comment;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.WriterResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SimpleCommentResponseDto {

    private String content;
    private WriterResponseDto writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedData;

    public static SimpleCommentResponseDto of(Comment comment, User user) {
        return SimpleCommentResponseDto.builder()
                .content(comment.getContent())
                .writer(new WriterResponseDto(user.getUserId(), user.getId(), user.getNickname()))
                .createdDate(comment.getCreatedDate())
                .modifiedData(comment.getModifiedDate())
                .build();
    }
}
