package com.sh.domain.board.controller;

import com.sh.domain.board.dto.BoardBasicResponseDto;
import com.sh.domain.board.dto.CreateBoardRequestDto;
import com.sh.domain.board.dto.UpdateBoardRequestDto;
import com.sh.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("/api/v1/board")
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateBoardRequestDto createRequest) {
        return boardService.createBoard(createRequest);
    }

    // 게시글 조회
    @GetMapping("/api/v1/board/{boardId}")
    public ResponseEntity<BoardBasicResponseDto> selectBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok()
                .body(boardService.selectBoard(boardId));
    }

    // 게시글 수정
    @PutMapping("/api/v1/board/{boardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modify(@PathVariable Long boardId, @RequestBody @Valid UpdateBoardRequestDto updateRequest) {
        boardService.modifyBoard(boardId,updateRequest);
    }

    // 게시글 삭제
    @DeleteMapping("/api/v1/board/{boardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
    }
}
