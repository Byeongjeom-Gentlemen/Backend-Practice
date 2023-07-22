package com.sh.domain.board.service;

import com.sh.domain.board.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface BoardService {

    // 게시글 생성
    @Transactional
    Long createBoard(CreateBoardRequestDto createRequest);

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    BoardBasicResponseDto selectBoard(Long boardId);

    // 게시글 수정
    @Transactional
    void modifyBoard(Long boardId, UpdateBoardRequestDto updateRequest);

    // 게시글 삭제
    @Transactional
    void deleteBoard(Long boardId);

    // 게시글 조회(전체 조회, 검색을 통한 조회)
    @Transactional(readOnly = true)
    PagingBoardsResponseDto searchBoards(Pageable pageable, String searchType, String keyword);
}
