package com.sh.domain.board.repository;

import com.sh.domain.board.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 좋아요 체크
    @Query("select b from Like b where b.user = :userId and b.board = :boardId")
    Optional<Like> findByUserAndBoard(Long userId, Long boardId);

    // 좋아요 저장
    Like save(Like like);

    // 좋아요 삭제
    void deleteByLikeId(Long likeId);
}
