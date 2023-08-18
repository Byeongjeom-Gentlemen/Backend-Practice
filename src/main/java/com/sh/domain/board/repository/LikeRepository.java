package com.sh.domain.board.repository;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.Like;
import com.sh.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 좋아요 체크
    @Query(value = "select b from Like b where b.user = :user and b.board = :board")
    // @Query(value = "select * from likes where user_id = :userId and board_id = :boardId",
    // nativeQuery = true)
    Optional<Like> findByUserAndBoard(User user, Board board);

    // 좋아요 저장
    Like save(Like like);

    // 좋아요 삭제
    void deleteByLikeId(Long likeId);
}
