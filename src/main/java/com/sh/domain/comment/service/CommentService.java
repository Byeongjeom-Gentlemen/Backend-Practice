package com.sh.domain.comment.service;

import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {

    // 댓글 등록
    @Transactional
    Long createComment(Long boardId, String content);

    // 댓글 조회
    @Transactional(readOnly = true)
    List<SimpleCommentResponseDto> selectCommentList(Pageable pageable, Long boardId);
}
