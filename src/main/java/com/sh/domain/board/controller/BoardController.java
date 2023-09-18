package com.sh.domain.board.controller;

import com.sh.domain.board.dto.request.CreateBoardRequestDto;
import com.sh.domain.board.dto.request.UpdateBoardRequestDto;
import com.sh.domain.board.dto.response.BoardBasicResponseDto;
import com.sh.domain.board.dto.response.BoardFileResponseDto;
import com.sh.domain.board.dto.response.SimpleBoardResponseDto;
import com.sh.domain.board.service.BoardFileService;
import com.sh.domain.board.service.BoardService;
import com.sh.global.aop.DisableSwaggerSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board", description = "Board API")
public class BoardController {

    private final BoardService boardService;
    private final BoardFileService boardFileService;

    // 게시글 생성
    @Operation(
            summary = "게시글 등록 API",
            description = "게시글을 등록하는 API 입니다. 로그인 여부와 Title 값을 필요로 하며 첨부파일을 등록할 수 있습니다.")
    @PostMapping("/api/v1/board")
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(
            @RequestPart(value = "createRequest") @Valid CreateBoardRequestDto createRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return boardService.createBoard(createRequest, files);
    }

    // 게시글 상세 조회
    @Operation(
            summary = "게시글 상세 조회 API",
            description = "게시글을 상세 조회하는 API 입니다. 게시글의 ID(PK) 값을 필요로 합니다.")
    @DisableSwaggerSecurity
    @GetMapping("/api/v1/board/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public BoardBasicResponseDto selectBoard(@PathVariable Long boardId) {
        return boardService.selectBoard(boardId);
    }

    // 게시글 첨부파일 조회
    @Operation(
            summary = "게시글 첨부파일 조회 API",
            description = "게시글을 첨부파일을 조회하는 API 입니다. 첨부파일의 저장된 이름, 저장된 경로, 종류를 반환합니다.")
    @DisableSwaggerSecurity
    @GetMapping("/api/v1/board/{boardId}/files")
    @ResponseStatus(HttpStatus.OK)
    public List<BoardFileResponseDto> selectBoardFiles(@PathVariable Long boardId) {
        return boardFileService.getBoardFiles(boardId);
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

    // 게시글 일부 첨부파일 삭제
    @DeleteMapping("/api/v1/board/{boardId}/{storeFileName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttachedFile(@PathVariable Long boardId, @PathVariable String storeFileName) {
        boardFileService.deleteAttachedFile(boardId, storeFileName);
    }

    // 게시글 조회(전체 조회, 검색어를 통한 조회)
    @Operation(
            summary = "게시글 조회 API(전체 조회, 검색을 통한 조회)",
            description =
                    "게시글을 조회하는 API 입니다. searchType 값이 null이면 전체 조회, 아니면 searchType과 keyword를 통한 조회입니다.")
    @DisableSwaggerSecurity
    @GetMapping("/api/v1/board")
    @ResponseStatus(HttpStatus.OK)
    public List<SimpleBoardResponseDto> searchBoard(
            @RequestParam(required = false) Long lastBoardId,
            @RequestParam(required = false, defaultValue = "all") String searchType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return boardService.searchBoards(lastBoardId, searchType, keyword, size);
    }

    // 게시글 좋아요 등록
    @Operation(summary = "게시글 좋아요 등록 API", description = "boardId 값을 통해 해당 게시글을 좋아요 할 수 있습니다.")
    @PostMapping("/api/v1/board/{boardId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeBoard(@PathVariable Long boardId) {
        boardService.addLikeCountUseRedisson(boardId);
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
