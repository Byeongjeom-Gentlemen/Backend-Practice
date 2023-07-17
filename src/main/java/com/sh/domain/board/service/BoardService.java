package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.dto.BoardCreateRequestDto;

import java.util.Optional;

public interface BoardService {

    // 게시글 생성
    Long createBoard(BoardCreateRequestDto board, String userId);
}
