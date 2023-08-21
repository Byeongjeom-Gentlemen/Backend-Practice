package com.sh.domain.board.controller;

import com.sh.domain.board.dto.request.CreateBoardRequestDto;
import com.sh.domain.board.dto.request.UpdateBoardRequestDto;
import com.sh.domain.board.dto.response.BoardBasicResponseDto;
import com.sh.domain.board.dto.response.PagingBoardsResponseDto;
import com.sh.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board", description = "Board API")
public class BoardController {

    private final BoardService boardService;
    private static final String LIKE_KEY_PREFIX = "LIKE_";

    // 게시글 생성
    @Operation(summary = "게시글 등록 API", description = "게시글을 등록하는 API 입니다. 로그인 여부와 Title 값을 필요로 합니다.")
    @PostMapping("/api/v1/board")
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateBoardRequestDto createRequest) {
        return boardService.createBoard(createRequest);
    }

    // 게시글 상세 조회
    @Operation(
            summary = "게시글 상세 조회 API",
            description = "게시글을 상세 조회하는 API 입니다. 게시글의 ID(PK) 값을 필요로 합니다.")
    @GetMapping("/api/v1/board/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public BoardBasicResponseDto selectBoard(@PathVariable Long boardId) {
        return boardService.selectBoard(boardId);
    }

    // 게시글 수정
    @Operation(
            summary = "게시글 수정 API",
            description = "게시글을 수정하는 API 입니다. 로그인 여부와 Title, Content 값을 필요로 합니다.")
    @PutMapping("/api/v1/board/{boardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modify(
            @PathVariable Long boardId, @RequestBody @Valid UpdateBoardRequestDto updateRequest) {
        boardService.modifyBoard(boardId, updateRequest);
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제하는 API 입니다. 로그인 여부를 필요로 합니다.")
    @DeleteMapping("/api/v1/board/{boardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
    }

    // 게시글 조회(전체 조회, 검색어를 통한 조회)
    @Operation(
            summary = "게시글 조회 API(전체 조회, 검색을 통한 조회)",
            description =
                    "게시글을 조회하는 API 입니다. searchType 값이 null이면 전체 조회, 아니면 searchType과 keyword를 통한 조회입니다.")
    @GetMapping("/api/v1/board")
    @ResponseStatus(HttpStatus.OK)
    public PagingBoardsResponseDto searchBoard(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)
                    @ParameterObject
                    Pageable pageable,
            @RequestParam(required = false, defaultValue = "all") String searchType,
            @RequestParam(required = false) String keyword) {
        return boardService.searchBoards(pageable, searchType, keyword);
    }

    // 게시글 좋아요 등록
    @Operation(
            summary = "게시글 좋아요 등록 API",
            description = "boardId 값을 통해 해당 게시글을 좋아요 할 수 있습니다.")
    @PostMapping("/api/v1/board/{boardId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeBoard(@PathVariable Long boardId) { boardService.addLikeCountUseRedisson(boardId);
    }

    // 게시글 좋아요 취소
    @Operation(
            summary = "게시글 좋아요 취소 API",
            description = "boardId 값을 통해 해당 게시글 좋아요를 누른 내역이 존재한다면 좋아요 기록을 취소할 수 있습니다.")
    @DeleteMapping("/api/v1/board/{boardId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void cancelLikeBoard(@PathVariable Long boardId) {
        boardService.minusLikeCountUseRedisson(boardId);
    }

}
