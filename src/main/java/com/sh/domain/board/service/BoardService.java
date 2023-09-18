package com.sh.domain.board.service;

import com.sh.domain.board.dto.request.CreateBoardRequestDto;
import com.sh.domain.board.dto.request.UpdateBoardRequestDto;
import com.sh.domain.board.dto.response.BoardBasicResponseDto;
import com.sh.domain.board.dto.response.SimpleBoardResponseDto;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    // 게시글 생성
    @Transactional
    Long createBoard(CreateBoardRequestDto createRequest, List<MultipartFile> files);

    // 게시글 상세 조회 (UnLock)
    @Transactional
    BoardBasicResponseDto selectBoardUnLock(Long boardId);

    // 게시글 상세 조회
    BoardBasicResponseDto selectBoard(Long boardId);

    // 게시글 수정
    @Transactional
    void modifyBoard(Long boardId, UpdateBoardRequestDto updateRequest, List<MultipartFile> files, List<Long> deleteFileIds);

    // 게시글 삭제
    @Transactional
    void deleteBoard(Long boardId);

    // 게시글 리스트 조회(전체 조회, 검색을 통한 조회) -> 페이징
    @Transactional(readOnly = true)
    List<SimpleBoardResponseDto> searchBoards(
            Long lastBoardId, String searchType, String keyword, int size);

    // 게시글 좋아요 등록 (UnLock)
    @Transactional
    void addLikeCount(Long boardId);

    // 게시글 좋아요 등록 (Redisson 분산 락)
    void addLikeCountUseRedisson(Long boardId);

    // 게시글 좋아요 취소 (Redisson 분산 락)
    void minusLikeCountUseRedisson(Long boardId);
}
