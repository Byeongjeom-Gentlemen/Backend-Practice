package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.dto.BoardBasicResponseDto;
import com.sh.domain.board.dto.BoardCreateRequestDto;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.customexcpetion.board.NotFoundBoardException;
import com.sh.global.exception.customexcpetion.user.UserNonLoginException;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    @Override
    public Long createBoard(BoardCreateRequestDto board, String userId) {
        // 로그인 안된 회원인 경우
        if(userId == null) {
            throw new UserNonLoginException();
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Board newBoard = Board.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .user(user)
                .build();

        return boardRepository.save(newBoard).getId();
    }

    // 게시글 조회
    @Override
    public BoardBasicResponseDto selectBoard(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        return BoardBasicResponseDto.from(board);
    }
}
