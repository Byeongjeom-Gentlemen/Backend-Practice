package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.dto.BoardBasicResponseDto;
import com.sh.domain.board.dto.CreateBoardRequestDto;
import com.sh.domain.board.dto.UpdateBoardRequestDto;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.BoardErrorCode;
import com.sh.global.exception.UserErrorCode;
import com.sh.global.exception.customexcpetion.board.NotFoundBoardException;
import com.sh.global.exception.customexcpetion.board.NotMatchesWriterException;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.util.SessionUtil;
import lombok.RequiredArgsConstructor;
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
        Long id = sessionUtil.getAttribute();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        Board newBoard = Board.builder()
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .user(user)
                .build();

        return boardRepository.save(newBoard).getId();
    }

    // 게시글 조회
    @Override
    public BoardBasicResponseDto selectBoard(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        if(board.getDelete_at() != null) {
            throw new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD);
        }

        return BoardBasicResponseDto.from(board);
    }

    // 게시글 수정
    @Override
    public void modifyBoard(Long boardId, UpdateBoardRequestDto updateRequest) {
        Long id = sessionUtil.getAttribute();

        // 해당 게시글이 존재하지 않을 경우
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        if(board.getDelete_at() != null) {
            throw new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD);
        }

        // 해당 게시글의 작성자가 다른 경우
        if(board.getUser().getId() != id) {
            throw new NotMatchesWriterException(BoardErrorCode.BOARD_NOT_AUTHORITY);
        }

        board.update(updateRequest);
    }

    // 게시글 삭제
    @Override
    public void deleteBoard(Long boardId) {
        Long id = sessionUtil.getAttribute();

        Board board = boardRepository.findById(boardId)
                        .orElseThrow(() -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        if(board.getDelete_at() != null) {
            throw new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD);
        }

        if(board.getUser().getId() != id) {
            throw new NotMatchesWriterException(BoardErrorCode.BOARD_NOT_AUTHORITY);
        }

        boardRepository.delete(board);
    }
}
