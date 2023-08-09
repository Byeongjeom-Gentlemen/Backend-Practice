package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.Like;
import com.sh.domain.board.dto.request.CreateBoardRequestDto;
import com.sh.domain.board.dto.request.UpdateBoardRequestDto;
import com.sh.domain.board.dto.response.BoardBasicResponseDto;
import com.sh.domain.board.dto.response.LikeResponseDto;
import com.sh.domain.board.dto.response.PagingBoardsResponseDto;
import com.sh.domain.board.dto.response.SimpleBoardResponseDto;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.board.repository.LikeRepository;
import com.sh.domain.board.util.SearchType;
import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import com.sh.domain.comment.service.CommentService;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.domain.user.service.UserService;
import com.sh.global.exception.customexcpetion.board.*;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.exception.errorcode.BoardErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final CommentService commentService;

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

    // 게시글 상세 조회 (해당 게시글의 댓글까지 조회)
    @Override
    public BoardBasicResponseDto selectBoard(Long boardId) {
        Board board = verificationBoard(boardId);

        // 게시글을 조회함과 동시에 해당 게시글의 댓글 정보도 조회
        // default 값으로 첫 페이지, 사이즈는 10개, 최신순으로 정렬되도록 설정
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");
        List<SimpleCommentResponseDto> commentList =
                commentService.selectCommentList(pageable, boardId).getCommentList();

        return BoardBasicResponseDto.of(board, commentList);
    }

    // 게시글 수정
    @Override
    public void modifyBoard(Long boardId, UpdateBoardRequestDto updateRequest) {
        Board board = verificationBoard(boardId);
        verificationWriter(board.getUser().getUserId());

        board.update(updateRequest);
    }

    // 게시글 삭제
    @Override
    public void deleteBoard(Long boardId) {
        Board board = verificationBoard(boardId);
        verificationWriter(board.getUser().getUserId());

        boardRepository.delete(board);
    }

    // 좋아요
    @Override
    public LikeResponseDto likeBoard(Long boardId) {
        User user = userService.getLoginUser();
        Board board = verificationBoard(boardId);

        Like like = likeRepository.findByUserAndBoard(user, board);
        String status = "";

        // 이미 해당 게시글의 좋아요를 누른 경우
        if (like != null) {
            board.minusLike();
            likeRepository.deleteByLikeId(like.getLikeId());
            status = "Cancel";
        }

        // 해당 게시글의 좋아요를 누르지 않은 경우
        if (like == null) {
            board.plusLike();
            like = likeRepository.save(Like.builder().user(user).board(board).build());
            status = "Press";
        }

        return LikeResponseDto.of(like.getLikeId(), status);
    }

    // 게시글 검증 로직
    @Override
    public Board verificationBoard(Long boardId) {
        // 해당 게시글이 존재하지 않을 경우
        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(
                                () -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        // 삭제된 게시글일 경우
        if (board.getDelete_at() != null) {
            throw new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD);
        }

        return board;
    }

    // 작성자 검증 로직
    private void verificationWriter(Long userId) {
        User user = userService.getLoginUser();

        // 해당 게시글의 작성자가 다른 경우
        if (userId != user.getUserId()) {
            throw new NotMatchesWriterException(BoardErrorCode.BOARD_NOT_AUTHORITY);
        }
    }

    // 게시글 리스트 조회 (전체 조회, 검색어를 통한 조회)
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
