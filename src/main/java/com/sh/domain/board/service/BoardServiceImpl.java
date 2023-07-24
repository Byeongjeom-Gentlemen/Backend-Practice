package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.dto.*;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.errorcode.BoardErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.exception.customexcpetion.board.*;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.util.SearchType;
import com.sh.global.util.SessionUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final SessionUtil sessionUtil;

    // 게시글 등록
    @Override
    public Long createBoard(CreateBoardRequestDto createRequest) {
        Long userId = sessionUtil.getAttribute();

        User user =
                userRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

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

        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(
                                () -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        if (board.getDelete_at() != null) {
            throw new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD);
        }

        return BoardBasicResponseDto.from(board);
    }

    // 게시글 수정
    @Override
    public void modifyBoard(Long boardId, UpdateBoardRequestDto updateRequest) {
        Long userId = sessionUtil.getAttribute();

        // 해당 게시글이 존재하지 않을 경우
        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(
                                () -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        if (board.getDelete_at() != null) {
            throw new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD);
        }

        // 해당 게시글의 작성자가 다른 경우
        if (board.getUser().getUserId() != userId) {
            throw new NotMatchesWriterException(BoardErrorCode.BOARD_NOT_AUTHORITY);
        }

        board.update(updateRequest);
    }

    // 게시글 삭제
    @Override
    public void deleteBoard(Long boardId) {
        Long userId = sessionUtil.getAttribute();

        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(
                                () -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        if (board.getDelete_at() != null) {
            throw new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD);
        }

        if (board.getUser().getUserId() != userId) {
            throw new NotMatchesWriterException(BoardErrorCode.BOARD_NOT_AUTHORITY);
        }

        boardRepository.delete(board);
    }

    // 게시글 조회(전체 조회, 검색어를 통한 조회)
    @Override
    public PagingBoardsResponseDto searchBoards(
            Pageable pageable, String searchType, String keyword) {
        SearchType type = SearchType.convertToType(searchType);

        if (type == null) {
            throw new UnsupportedSearchTypeException(BoardErrorCode.UNSUPPORTED_SEARCH_TYPE);
        }

        // 게시글 조회에서는 Page<>를 사용해 구현하고자 함
        Page<Board> pages = null;

        // 전체 조회
        if (type == SearchType.ALL) {
            pages = boardRepository.findAll(pageable);

            if (pages.getContent().isEmpty()) {
                throw new BoardListIsEmptyException(BoardErrorCode.EMPTY_BOARD_LIST);
            }
        }

        // 제목으로 게시글 조회
        if (type == SearchType.TITLE) {
            if (keyword == null || keyword.equals("")) {
                throw new SearchKeywordIsEmptyException(BoardErrorCode.KEYWORD_EMPTY);
            }

            // 해당 제목을 포함한 게시글이 있다면 pageable 값에 따라 데이터 저장
            pages = boardRepository.findByTitleContaining(keyword, pageable);

            // 설정한 page에 데이터가 없을 경우
            if (pages.getContent().isEmpty()) {
                throw new NotFoundBoardException(BoardErrorCode.NOT_FOUND_SEARCH_TITLE);
            }
        }

        // 작성자로 게시글 조회
        if (type == SearchType.WRITER) {
            if (keyword == null || keyword.equals("")) {
                throw new SearchKeywordIsEmptyException(BoardErrorCode.KEYWORD_EMPTY);
            }

            // 해당 닉네임을 가진 유저가 있는지 확인, 없으면 예외처리
            User user =
                    userRepository
                            .findByNickname(keyword)
                            .orElseThrow(
                                    () -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

            // 해당 유저가 작성한 게시글이 있다면 pageable 값에 따라 데이터 저장
            pages = boardRepository.findByUserId(user.getUserId(), pageable);

            // 설정한 page에 데이터가 없을 경우
            if (pages.getContent().isEmpty()) {
                throw new NotFoundBoardException(BoardErrorCode.NOT_FOUND_SEARCH_WRITER);
            }
        }

        List<SimpleBoardResponseDto> boardList =
                pages.getContent().stream()
                        .filter(board -> board.getDelete_at() == null)
                        .map(board -> SimpleBoardResponseDto.from(board))
                        .collect(Collectors.toList());

        return PagingBoardsResponseDto.of(pages, boardList);
    }
}
