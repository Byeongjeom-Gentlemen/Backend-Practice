package com.sh.domain.board.controller;

import com.sh.domain.board.dto.BoardBasicResponseDto;
import com.sh.domain.board.dto.BoardCreateRequestDto;
import com.sh.domain.board.dto.UpdateBoardRequestDto;
import com.sh.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("/api/v1/board")
    public ResponseEntity<Long> create(@RequestBody @Valid BoardCreateRequestDto board,
                                       @SessionAttribute(name = "userId", required = false) String userId) {
        return ResponseEntity.created(URI.create("/api/v1/board"))
                .body(boardService.createBoard(board, userId));
    }

    // 게시글 조회
    @GetMapping("/api/v1/board/{boardId}")
    public ResponseEntity<BoardBasicResponseDto> selectBoard(@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok()
                .body(boardService.selectBoard(boardId));
    }

    // 게시글 수정
    @PutMapping("/api/v1/board/{boardId}")
    public ResponseEntity<BoardBasicResponseDto> modify(@PathVariable("boardId") Long boardId,
                                                        @SessionAttribute(name = "userId", required = false) String userId,
                                                        @RequestBody @Valid UpdateBoardRequestDto afterBoard) {
        boardService.modifyBoard(boardId, userId, afterBoard);
        return ResponseEntity.noContent().build();
    }

    // 게시글 삭제
    @DeleteMapping("/api/v1/board/{boardId}")
    public ResponseEntity<BoardBasicResponseDto> delete(@PathVariable("boardId") Long boardId,
                                                        @SessionAttribute(name = "userId", required = false) String userId) {
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.noContent().build();
    }
}
