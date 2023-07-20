package com.sh.domain.board.dto;

import com.sh.domain.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PagingBoardsResponseDto {

    // 총 페이지
    private Integer totalPage;
    
    // 총 데이터 수
    private Long totalData;
    
    // 현재 페이지
    private Integer curPage;
    
    // 페이지 당 데이터 수
    private Integer pageSize;
    
    // 게시글 리스트
    private List<SimpleBoardResponseDto> boards;


    public static PagingBoardsResponseDto of(Page<Board> pageable, List<SimpleBoardResponseDto> boardList) {
        return PagingBoardsResponseDto.builder()
                .totalPage(pageable.getTotalPages())
                .totalData(pageable.getTotalElements())
                .curPage(pageable.getPageable().getPageNumber() + 1)
                .pageSize(pageable.getPageable().getPageSize())
                .boards(boardList)
                .build();
    }
}
