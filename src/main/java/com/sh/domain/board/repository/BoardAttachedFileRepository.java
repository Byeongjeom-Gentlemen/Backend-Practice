package com.sh.domain.board.repository;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.BoardAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardAttachedFileRepository extends JpaRepository<BoardAttachedFile, Long> {

    List<BoardAttachedFile> findAllByBoard(Board board);
}
