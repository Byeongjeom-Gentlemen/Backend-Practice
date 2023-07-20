package com.sh.domain.board.repository;

import com.sh.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 게시글 생성
    Board save(Board board);

    // 게시글 조회
    Optional<Board> findById(Long boardId);

    // 게시글 삭제
    void delete(Board board);
}
