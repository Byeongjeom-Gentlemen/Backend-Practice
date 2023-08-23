package com.sh.domain.comment.repository;

import com.sh.domain.board.domain.Board;
import com.sh.domain.comment.domain.Comment;
import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepositoryCustom {
    // 댓글 조회 (무한 스크롤) - querydsl
    Slice<SimpleCommentResponseDto> findByCommentIdLessThanOrderByCreatedAtDesc(Long lastCommentId, Long boardId, Pageable pageable);
}
