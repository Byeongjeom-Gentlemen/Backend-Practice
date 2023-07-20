package com.sh.domain.board.controller;

import com.sh.domain.board.dto.BoardBasicResponseDto;
import com.sh.domain.board.dto.CreateBoardRequestDto;
import com.sh.domain.board.dto.PagingBoardsResponseDto;
import com.sh.domain.board.dto.UpdateBoardRequestDto;
import com.sh.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    // 게시글 조회
    @Operation(summary = "게시글 조회 API", description = "게시글을 조회하는 API 입니다. 게시글의 ID(PK) 값을 필요로 합니다.")
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

    // 게시글 전체 조회
    @Operation(summary = "게시글 전체 조회 API", description = "게시글 전체를 조회하는 API 입니다. 페이지 넘버와 보여질 데이터 수를 필요로 합니다.")
    @GetMapping("/api/v1/board")
    public ResponseEntity<PagingBoardsResponseDto> allBoards(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                             @RequestParam(name = "count", required = false, defaultValue = "5") Integer count) {
        PageRequest pageable = PageRequest.of(page, count);
        return ResponseEntity.ok().body(boardService.allBoards(pageable));
    }
}
