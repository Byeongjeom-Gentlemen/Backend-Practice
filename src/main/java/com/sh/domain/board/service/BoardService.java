package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.dto.BoardBasicResponseDto;
import com.sh.domain.board.dto.BoardCreateRequestDto;
import com.sh.domain.board.dto.UpdateBoardRequestDto;

import java.util.Optional;

public interface BoardService {

    // 게시글 생성
    Long createBoard(BoardCreateRequestDto board, String userId);

    // 게시글 조회
    BoardBasicResponseDto selectBoard(Long boardId);

    // 게시글 수정
    void modifyBoard(Long boardId, String userId, UpdateBoardRequestDto afterBoard);

    // 게시글 삭제
    void deleteBoard(Long boardId, String userId);
}
