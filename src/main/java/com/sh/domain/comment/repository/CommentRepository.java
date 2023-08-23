package com.sh.domain.comment.repository;

import com.sh.domain.comment.domain.Comment;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    // 댓글 등록
    Comment save(Comment comment);

    // 게시글 번호로 댓글 조회
    Slice<Comment> findByBoardId(Long boardId, Pageable pageable);

    // 댓글 번호로 댓글 조회
    Optional<Comment> findByCommentId(Long commentId);

    // 댓글 삭제
    void delete(Comment comment);

    // 댓글 조회 (무한 스크롤) - JPQL
    @Query(
            value =
                    "select c.commentId, c.content, c.user, c.createdDate, c.modifiedDate from Comment c where c.commentId < ?1 and c.board.id = ?2 and c.delete_at IS NULL order by c.commentId desc")
    Slice<Comment> findByCommentIdLessThanOrderByCommentIdDescInJpql(
            Long lastCommentId, Long boardId, Pageable pageable);
}
