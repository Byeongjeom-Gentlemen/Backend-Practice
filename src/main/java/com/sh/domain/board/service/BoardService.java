package com.sh.domain.board.service;

import com.sh.domain.board.dto.BoardBasicResponseDto;
import com.sh.domain.board.dto.CreateBoardRequestDto;
import com.sh.domain.board.dto.UpdateBoardRequestDto;
import org.springframework.transaction.annotation.Transactional;

public interface BoardService {

    @Transactional
    // 게시글 생성
    Long createBoard(CreateBoardRequestDto createRequest);

    @Transactional(readOnly = true)
    // 게시글 조회
    BoardBasicResponseDto selectBoard(Long boardId);

    @Transactional
    // 게시글 수정
    void modifyBoard(Long boardId, UpdateBoardRequestDto updateRequest);

    @Transactional
    // 게시글 삭제
    void deleteBoard(Long boardId);
}
