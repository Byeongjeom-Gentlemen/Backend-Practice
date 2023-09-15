package com.sh.domain.board.repository;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.BoardAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardAttachedFileRepository extends JpaRepository<BoardAttachedFile, Long> {

    List<BoardAttachedFile> findAllByBoard(Board board);

    @Query(value = "select b from BoardAttachedFile b where b.board = :board and b.storeFileName = :storeFileName")
    Optional<BoardAttachedFile> findByBoardAndStoreFileName(Board board, String storeFileName);
}
