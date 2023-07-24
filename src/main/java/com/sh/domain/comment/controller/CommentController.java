package com.sh.domain.comment.controller;

import com.sh.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment", description = "Comment API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글을 등록하는 API", description = "session에 저장되어 있는 id 값과 boardId 값으로 댓글을 등록할 수 있는지 검사하고 content 값으로 댓글을 등록합니다.")
    @PostMapping("/api/v1/board/{boardId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createComment(@PathVariable Long boardId,
                              @RequestParam("content") @NotBlank(message = "내용은 필수 입력 값 입니다.") String content) {
        return commentService.createComment(boardId, content);
    }
}
