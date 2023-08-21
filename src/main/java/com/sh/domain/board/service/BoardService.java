package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.dto.request.CreateBoardRequestDto;
import com.sh.domain.board.dto.request.UpdateBoardRequestDto;
import com.sh.domain.board.dto.response.BoardBasicResponseDto;
import com.sh.domain.board.dto.response.PagingBoardsResponseDto;
import com.sh.global.aop.DistributedLock;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface BoardService {

    // 게시글 생성
    @Transactional
    Long createBoard(CreateBoardRequestDto createRequest);

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    BoardBasicResponseDto selectBoard(Long boardId);

    // 게시글 조회
    @Transactional(readOnly = true)
    Board queryBoard(Long boardId);

    // 게시글 수정
    @Transactional
    void modifyBoard(Long boardId, UpdateBoardRequestDto updateRequest);

    // 게시글 삭제
    @Transactional
    void deleteBoard(Long boardId);

    // 게시글 리스트 조회(전체 조회, 검색을 통한 조회) -> 페이징
    @Transactional(readOnly = true)
    PagingBoardsResponseDto searchBoards(Pageable pageable, String searchType, String keyword);

    // 게시글 좋아요 등록 (락 X)
    @Transactional
    void addLikeCount(Long boardId);

    // 게시글 좋아요 등록 (Redisson 분산 락)
    void addLikeCountUseRedisson(Long boardId);

    // 게시글 좋아요 취소 (Redisson 분산 락)
    void minusLikeCountUseRedisson(Long boardId);
}
