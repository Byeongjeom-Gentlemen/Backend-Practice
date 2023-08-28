package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.Like;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.board.repository.LikeRepository;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.service.UserService;
import com.sh.global.aop.DistributedLock;
import com.sh.global.exception.customexcpetion.BoardCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;

    // 게시글 좋아요 등록
    @DistributedLock(key = "#key")
    public void addLikeCount(String key, Board board) {
        // 요청 한 사용자(로그인 한 사용자)
        User user = userService.getLoginUser();

        // 좋아요 기록 조회
        Like like = likeRepository.findByUserAndBoard(user, board).orElse(null);

        // 이미 해당 게시글 좋아요를 누른 상태라면, Exception
        if (like != null) {
            throw BoardCustomException.ALREADY_PRESSED_LIKE;
        }

        like = Like.builder().user(user).board(board).build();
        likeRepository.save(like);

        board.plusLike();
    }

    // 게시글 좋아요 취소
    @DistributedLock(key = "#key")
    public void decreaseLikeCount(String key, Board board) {
        // 요청 한 사용자(로그인 한 사용자)
        User user = userService.getLoginUser();

        // 좋아요 기록 조회, 좋아요 기록이 존재하지 않는다면 Exception
        Like like =
                likeRepository
                        .findByUserAndBoard(user, board)
                        .orElseThrow(() -> BoardCustomException.NOT_FOUND_LIKE);
        // 좋아요 기록 삭제
        likeRepository.delete(like);

        board.minusLike();
    }
}
