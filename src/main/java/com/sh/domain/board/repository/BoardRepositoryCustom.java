package com.sh.domain.board.repository;

import com.sh.domain.board.dto.response.SimpleBoardResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    // 게시글 조회 (검색)
    List<SimpleBoardResponseDto> findByBoardIdLessThanBoardInOrderByBoardIdDescAndSearch(
            Long lastBoardId, String searchType, String keyword, Pageable pageable);
}
