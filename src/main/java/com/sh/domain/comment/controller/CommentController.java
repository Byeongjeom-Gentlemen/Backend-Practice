package com.sh.domain.comment.controller;

import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import com.sh.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment", description = "Comment API")
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @Operation(
            summary = "댓글을 등록하는 API",
            description =
                    "session에 저장되어 있는 id 값과 boardId 값으로 댓글을 등록할 수 있는지 검사하고 content 값으로 댓글을 등록합니다.")
    @PostMapping("/api/v1/board/{boardId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createComment(
            @PathVariable Long boardId,
            @RequestParam("content") @NotBlank(message = "내용은 필수 입력 값 입니다.") String content) {
        return commentService.createComment(boardId, content);
    }

    // 댓글 조회
    @Operation(
            summary = "댓글을 조회하는 API",
            description = "boardId와 page, size 값에 따라 최신 등록순으로 댓글을 조회합니다.")
    @GetMapping("/api/v1/board/{boardId}/comment")
    public ResponseEntity<List<SimpleCommentResponseDto>> selectComment(
            @PageableDefault(
                            page = 0,
                            size = 10,
                            sort = "commentId",
                            direction = Sort.Direction.DESC)
                    Pageable pageable,
            @RequestParam("boardId") Long boardId) {
        return ResponseEntity.ok().body(commentService.selectCommentList(pageable, boardId));
    }

    // 댓글 수정
    @Operation(summary = "댓글 수정 API", description = "content 값으로 댓글을 수정합니다.")
    @PatchMapping("/api/v1/board/{boardId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestParam("content") @NotBlank(message = "내용은 필수 입력 값입니다.") String updateRequest) {
        commentService.updateComment(boardId, commentId, updateRequest);
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제 API", description = "commentId 값으로 댓글을 삭제합니다.")
    @DeleteMapping("/api/v1/board/{boardId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long boardId, @PathVariable Long commentId) {
        commentService.deleteComment(boardId, commentId);
    }
}