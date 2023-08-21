package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.Like;
import com.sh.domain.board.dto.request.CreateBoardRequestDto;
import com.sh.domain.board.dto.request.UpdateBoardRequestDto;
import com.sh.domain.board.dto.response.BoardBasicResponseDto;
import com.sh.domain.board.dto.response.PagingBoardsResponseDto;
import com.sh.domain.board.dto.response.SimpleBoardResponseDto;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.board.repository.LikeRepository;
import com.sh.domain.board.util.SearchType;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.domain.user.service.UserService;
import com.sh.global.exception.customexcpetion.BoardCustomException;
import com.sh.global.exception.customexcpetion.CommonCustomException;
import com.sh.global.exception.customexcpetion.PageCustomException;
import com.sh.global.exception.customexcpetion.UserCustomException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    public PagingBoardsResponseDto searchBoards(
            Pageable pageable, String searchType, String keyword) {

        // pageNumber or pageSize 가 음수일 경우
        if (pageable.getPageNumber() < 0 || pageable.getPageSize() < 0) {
            throw PageCustomException.NEGATIVE_NUMBER;
        }

        // pageSize 가 max 값을 초과할 경우
        if (pageable.getPageSize() > 10) {
            throw PageCustomException.RANGE_OVER;
        }

        SearchType type = SearchType.convertToType(searchType.toLowerCase());

        if (type == null) {
            throw BoardCustomException.UNSUPPORTED_SEARCH_TYPE;
        }

        // 게시글 조회에서는 Page<>를 사용해 구현하고자 함
        Page<Board> pages = null;

        // 전체 조회
        if (type == SearchType.ALL) {
            pages = boardRepository.findAll(pageable);
        }

        // 제목으로 게시글 조회
        if (type == SearchType.TITLE) {
            if (keyword == null || keyword.equals("")) {
                throw BoardCustomException.SEARCH_KEYWORD_IS_EMPTY;
            }

            // 해당 제목을 포함한 게시글이 있다면 pageable 값에 따라 데이터 저장
            pages = boardRepository.findByTitleContaining(keyword, pageable);

            // 설정한 page에 데이터가 없을 경우
            if (pages.getContent().isEmpty()) {
                throw BoardCustomException.NOT_FOUND_BOARD_FOR_TITLE;
            }
        }

        // 작성자로 게시글 조회
        if (type == SearchType.WRITER) {
            if (keyword == null || keyword.equals("")) {
                throw BoardCustomException.SEARCH_KEYWORD_IS_EMPTY;
            }

            // 해당 닉네임을 가진 유저가 있는지 확인, 없으면 예외처리
            User user =
                    userRepository
                            .findByNickname(keyword)
                            .orElseThrow(() -> UserCustomException.USER_NOT_FOUND);

            // 해당 유저가 작성한 게시글이 있다면 pageable 값에 따라 데이터 저장
            pages = boardRepository.findByUserId(user.getUserId(), pageable);

            // 설정한 page에 데이터가 없을 경우
            if (pages.getContent().isEmpty()) {
                throw BoardCustomException.NOT_FOUND_BOARD_FOR_WRITER;
            }
        }

        List<SimpleBoardResponseDto> boardList =
                pages.getContent().stream()
                        .filter(board -> board.getDelete_at() == null)
                        .map(board -> SimpleBoardResponseDto.from(board))
                        .collect(Collectors.toList());

        return PagingBoardsResponseDto.of(pages, boardList);
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
