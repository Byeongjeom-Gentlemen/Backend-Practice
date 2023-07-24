package com.sh.domain.comment.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.comment.domain.Comment;
import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import com.sh.domain.comment.repository.CommentRepository;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.errorcode.BoardErrorCode;
import com.sh.global.exception.errorcode.CommentErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.exception.customexcpetion.board.NotFoundBoardException;
import com.sh.global.exception.customexcpetion.comment.NotAuthorityException;
import com.sh.global.exception.customexcpetion.comment.NotFoundCommentException;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.util.SessionUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

        User user =
                userRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(
                                () -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        Comment comment = Comment.builder().content(content).board(board).user(user).build();

        return commentRepository.save(comment).getCommentId();
    }

    // 댓글 조회
    @Override
    public List<SimpleCommentResponseDto> selectCommentList(Pageable pageable, Long boardId) {

        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(
                                () -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        List<SimpleCommentResponseDto> commentList =
                commentRepository.findByBoardId(boardId, pageable).getContent().stream()
                        .filter(comment -> comment.getDelete_at() == null)
                        .map(comment -> SimpleCommentResponseDto.of(comment, comment.getUser()))
                        .collect(Collectors.toList());

        return commentList;
    }

    // 댓글 수정
    @Override
    public void updateComment(Long boardId, Long commentId, String updateRequest) {
        Long userId = sessionUtil.getAttribute();

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new NotFoundCommentException(CommentErrorCode.NOT_FOUND_COMMENT));

        if(userId != comment.getUser().getUserId()) {
            throw new NotAuthorityException(CommentErrorCode.NOT_AUTHORITY_COMMENT);
        }

        comment.update(updateRequest);
    }
}
