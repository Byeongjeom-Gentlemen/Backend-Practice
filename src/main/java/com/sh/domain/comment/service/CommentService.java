package com.sh.domain.comment.service;

import com.sh.domain.comment.dto.CommentListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {

    // 댓글 등록
    @Transactional
    Long createComment(Long boardId, String content);

    // 댓글 리스트 조회 -> 페이징
    @Transactional(readOnly = true)
    CommentListResponseDto selectCommentList(Long boardId, Long lastCommentId);

    // 댓글 수정
    @Transactional
    void updateComment(Long boardId, Long commentId, String updateRequest);

    // 댓글 삭제
    @Transactional
    void deleteComment(Long boardId, Long commentId);
}
