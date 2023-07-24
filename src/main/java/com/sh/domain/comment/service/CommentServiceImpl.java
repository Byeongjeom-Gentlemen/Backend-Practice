package com.sh.domain.comment.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.comment.domain.Comment;
import com.sh.domain.comment.repository.CommentRepository;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.BoardErrorCode;
import com.sh.global.exception.UserErrorCode;
import com.sh.global.exception.customexcpetion.board.NotFoundBoardException;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final SessionUtil sessionUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Override
    public Long createComment(Long boardId, String content) {
        Long userId = sessionUtil.getAttribute();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        Comment comment = Comment.builder()
                .content(content)
                .board(board)
                .user(user)
                .build();

        return commentRepository.save(comment).getCommentId();
    }
}
