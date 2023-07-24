package com.sh.domain.comment.service;

import org.springframework.transaction.annotation.Transactional;

public interface CommentService {

    // 댓글 등록
    @Transactional
    Long createComment(Long boardId, String content);
}
