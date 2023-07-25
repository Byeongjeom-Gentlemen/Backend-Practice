package com.sh.domain.board.controller;

import com.sh.domain.board.dto.*;
import com.sh.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board", description = "Board API")
public class BoardController {

    private final BoardService boardService;

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
    public ResponseEntity<BoardBasicResponseDto> selectBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok().body(boardService.selectBoard(boardId));
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
    public ResponseEntity<PagingBoardsResponseDto> searchBoard(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)
                    Pageable pageable,
            @RequestParam(required = false, defaultValue = "all") String searchType,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok().body(boardService.searchBoards(pageable, searchType, keyword));
    }

    // 게시글 좋아요
    @Operation(summary = "좋아요 API", description = "boardId 값으로 해당 게시글의 좋아요 값을 count 합니다.")
    @PostMapping("/api/v1/board/{boardId}/like")
    @ResponseStatus(HttpStatus.OK)
    public Long createLike(@PathVariable Long boardId) {
        return boardService.createLike(boardId);
    }

    // 게시글 좋아요 취소
    @Operation(summary = "좋아요 취소 API", description = "boardId 값으로 해당 게시글의 좋아요 값을 minus 합니다.")
    @DeleteMapping("/api/v1/board/{boardId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long boardId) {
        boardService.deleteLike(boardId);
    }
}
