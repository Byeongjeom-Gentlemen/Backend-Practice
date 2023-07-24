package com.sh.domain.comment.repository;

import com.sh.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 댓글 등록
    Comment save(Comment comment);
}
