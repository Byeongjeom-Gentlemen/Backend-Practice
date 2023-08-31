package com.sh;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.board.service.BoardService;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.request.SignupRequestDto;
import com.sh.domain.user.repository.UserRepository;
import com.sh.domain.user.service.UserService;
import com.sh.global.exception.customexcpetion.BoardCustomException;
import com.sh.global.exception.customexcpetion.UserCustomException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardApplicationTests {

    @Autowired private UserService userService;

    @Autowired private UserRepository userRepository;

    @Autowired private BoardService boardService;

    @Autowired private BoardRepository boardRepository;

    private Board board;

    @BeforeEach
    void setUp() {
        SignupRequestDto request = new SignupRequestDto("ehftozl", "thdgus!", "헬로우");
        userService.join(request);

        User user =
                userRepository.findById(1L).orElseThrow(() -> UserCustomException.USER_NOT_FOUND);

        board = Board.builder().title("헬로우").content("바이여").user(user).build();
        boardRepository.save(board);
    }

    /*
    @Test
    void 동시_100명_좋아요_요청() throws InterruptedException {
    	int threadCount = 100;
    	ExecutorService executorService = Executors.newFixedThreadPool(32);
    	CountDownLatch latch = new CountDownLatch(threadCount);

    	for (int i = 0; i < threadCount; i++) {
    		executorService.submit(
    				() -> {
    					try {
    						// 락을 적용한 경우
    						boardService.addLikeCountUseRedisson(1L);
    						// 락을 적용하지 않은 경우
    						// boardService.addLikeCount(1L);
    					} finally {
    						latch.countDown();
    					}
    				});
    	}

    	latch.await();

    	Board b =
    			boardRepository
    					.findById(board.getId())
    					.orElseThrow(() -> BoardCustomException.BOARD_NOT_FOUND);

    	System.out.println("게시글 좋아요 수 : " + b.getLikeCount());
    	Assertions.assertEquals(b.getLikeCount(), threadCount);
    }
     */

    @Test
    void 동시_100명_게시글상세조회_조회수() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(
                    () -> {
                        try {
                            // 락을 적용한 경우
                            boardService.selectBoard("VIEW_COUNT_" + board.getId(), 1L);
                            // 락을 적용하지 않은 경우
                            // boardService.selectBoardUnLock(1L);
                        } finally {
                            latch.countDown();
                        }
                    });
        }

        latch.await();

        Board b =
                boardRepository
                        .findById(1L)
                        .orElseThrow(() -> BoardCustomException.BOARD_NOT_FOUND);

        System.out.println("게시글 조회수 : " + b.getViewCount());
        Assertions.assertEquals(b.getViewCount(), threadCount);
    }
}
