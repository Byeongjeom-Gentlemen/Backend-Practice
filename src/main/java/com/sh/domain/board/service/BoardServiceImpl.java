package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.dto.request.CreateBoardRequestDto;
import com.sh.domain.board.dto.request.UpdateBoardRequestDto;
import com.sh.domain.board.dto.response.BoardBasicResponseDto;
import com.sh.domain.board.dto.response.SimpleBoardResponseDto;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.board.repository.LikeRepository;
import com.sh.domain.board.util.SearchType;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.domain.user.service.UserService;
import com.sh.global.exception.customexcpetion.BoardCustomException;
import com.sh.global.exception.customexcpetion.PageCustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final UserService userService;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final LikeService likeService;

    // 게시글 등록
    @Override
    public Long createBoard(CreateBoardRequestDto createRequest) {
        User user = userService.getLoginUser();

        Board newBoard =
                Board.builder()
                        .title(createRequest.getTitle())
                        .content(createRequest.getContent())
                        .user(user)
                        .build();

        return boardRepository.save(newBoard).getId();
    }

    // 게시글 상세 조회
    @Override
    public BoardBasicResponseDto selectBoard(Long boardId) {
        Board board = queryBoard(boardId);
        board.verification();

        return BoardBasicResponseDto.from(board);
    }

    // 게시글 수정
    @Override
    public void modifyBoard(Long boardId, UpdateBoardRequestDto updateRequest) {
        Board board = queryBoard(boardId);
        board.verification();
        checkWriter(board.getUser().getUserId());

        board.update(updateRequest.getTitle(), updateRequest.getContent());
    }

    // 게시글 삭제
    @Override
    public void deleteBoard(Long boardId) {
        Board board = queryBoard(boardId);
        board.verification();
        checkWriter(board.getUser().getUserId());

        boardRepository.delete(board);
    }

    // 게시글 조회
    @Override
    public Board queryBoard(Long boardId) {
        return boardRepository
                .findById(boardId)
                .orElseThrow(() -> BoardCustomException.BOARD_NOT_FOUND);
    }

    // 작성자 검사
    private void checkWriter(Long userId) {
        User user = userService.getLoginUser();

        // 해당 게시글의 작성자가 다른 경우
        if (userId != user.getUserId()) {
            throw BoardCustomException.NOT_MATCHES_WRITER;
        }
    }

    // 게시글 리스트 조회 (전체 조회, 검색어를 통한 조회)
    @Override
    public List<SimpleBoardResponseDto> searchBoards(
            Long lastBoardId, String searchType, String keyword, int size) {

        // size 가 범위를 벗어난 경우
        if (size < 0 || size > 10) {
            throw PageCustomException.RANGE_OVER;
        }

        SearchType type = SearchType.convertToType(searchType.toLowerCase());
        // searchType 이 지원하지 않는 타입일 경우
        if (type == null) {
            throw BoardCustomException.UNSUPPORTED_SEARCH_TYPE;
        }

        // keyword 가 null 이거나 빈 값일 경우
        if (type != SearchType.ALL && (keyword == null || keyword.equals(""))) {
            throw BoardCustomException.SEARCH_KEYWORD_IS_EMPTY;
        }

        PageRequest pageable = PageRequest.of(0, size);
        return boardRepository.findByBoardIdLessThanBoardInOrderByBoardIdDescAndSearch(
                lastBoardId, type.getType(), keyword, pageable);
    }

    // 게시글 좋아요 추가
    @Override
    public void addLikeCount(Long boardId) {
        // 게시글 검증
        Board board = queryBoard(boardId);
        board.verification();

        board.plusLike();
    }

    // 게시글 좋아요 추가 (Redisson)
    @Override
    public void addLikeCountUseRedisson(Long boardId) {
        String key = "LIKE_" + boardId;
        likeService.addLikeCount(key, boardId);
    }

    // 게시글 좋아요 취소 (Redisson)
    @Override
    public void minusLikeCountUseRedisson(Long boardId) {
        String key = "LIKE_" + boardId;
        likeService.decreaseLikeCount(key, boardId);
    }
}
