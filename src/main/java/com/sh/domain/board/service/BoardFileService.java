package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.BoardAttachedFile;
import com.sh.domain.board.dto.response.BoardFileResponseDto;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface BoardFileService {

    // 게시글 첨부파일 업로드
    List<BoardAttachedFile> uploadAttachedFiles(Board board, List<MultipartFile> files);

    // 게시글 첨부파일 조회
    @Transactional(readOnly = true)
    List<BoardFileResponseDto> getBoardFiles(Long boardId);

    // 게시글 첨부파일 삭제
    void deleteAttachedFiles(List<BoardAttachedFile> files);

    // 게시글 첨부파일 수정
    @Transactional
    void modifyAttachedFiles(Long boardId, List<MultipartFile> files, List<Long> deleteFileIds);
}
