package com.sh.domain.board.controller;

import com.sh.domain.board.dto.BoardCreateRequestDto;
import com.sh.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.Valid;
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
}
