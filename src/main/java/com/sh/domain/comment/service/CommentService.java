package com.sh.domain.comment.service;

import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Temporal;

public interface CommentService {

    // 댓글 등록
    @Transactional
    Long createComment(Long boardId, String content);

    // 댓글 조회
    @Transactional(readOnly = true)
    List<SimpleCommentResponseDto> selectCommentList(Pageable pageable, Long boardId);

    // 댓글 수정
    @Transactional
    void updateComment(Long boardId, Long commentId, String updateRequest);

    // 댓글 삭제
    @Transactional
    void deleteComment(Long boardId, Long commentId);
}
