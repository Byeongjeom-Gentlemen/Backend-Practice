package com.sh.domain.comment.repository;

import com.sh.domain.comment.domain.Comment;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 댓글 등록
    Comment save(Comment comment);

    // 게시글 번호로 댓글 조회
    Slice<Comment> findByBoardId(Long boardId, Pageable pageable);

    // 댓글 번호로 댓글 조회
    Optional<Comment> findByCommentId(Long commentId);

    // 댓글 삭제
    void delete(Comment comment);
}
