package com.sh.domain.comment.repository;

import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentRepositoryCustom {
    // 댓글 조회 (무한 스크롤) - querydsl
    Slice<SimpleCommentResponseDto> findByCommentIdLessThanOrderByCommentIdDesc(
            Long lastCommentId, Long boardId, Pageable pageable);
}
