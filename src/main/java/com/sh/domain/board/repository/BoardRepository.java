package com.sh.domain.board.repository;

import com.sh.domain.board.domain.Board;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 게시글 생성
    Board save(Board board);

    // 게시글 상세 조회
    Optional<Board> findById(Long boardId);

    // 게시글 삭제
    void delete(Board board);

    // 게시글 전체 조회
    Page<Board> findAll(Pageable pageable);

    // 제목으로 게시글 검색
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    // 작성자로 게시글 검색
    Page<Board> findByUserId(Long userId, Pageable pageable);

    // 비관적 락 & 베타 락(읽기, 쓰기 모두 Lock을 검)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Board b where b.id = :boardId")
    Optional<Board> findByIdForUpdate(Long boardId);
}
