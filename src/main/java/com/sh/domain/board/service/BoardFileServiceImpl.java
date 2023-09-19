package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.BoardAttachedFile;
import com.sh.domain.board.dto.response.BoardFileResponseDto;
import com.sh.domain.board.repository.BoardAttachedFileRepository;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.global.exception.customexcpetion.BoardCustomException;
import com.sh.global.util.file.FileResponseDto;
import com.sh.global.util.file.FileUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardFileServiceImpl implements BoardFileService {

    @Value("${custom.boardFile-upload-path}")
    private String uploadPath;

    private final FileUtils fileUtils;
    private final BoardRepository boardRepository;
    private final BoardAttachedFileRepository attachedFileRepository;

    // 첨부파일 업로드 및 게시글 첨부파일 목록 반환
    @Override
    public List<BoardAttachedFile> uploadAttachedFiles(Board board, List<MultipartFile> files) {
        // 실제 경로에 파일 업로드
        List<FileResponseDto> fileList = fileUtils.uploadFiles(uploadPath, files);

        // 업로드한 파일 정보를 BoardAttachedFile 형으로 변환
        List<BoardAttachedFile> attachedFiles =
                fileList.stream()
                        .map(
                                attachedFile ->
                                        BoardAttachedFile.builder()
                                                .board(board)
                                                .storeFileName(attachedFile.getStoreFileName())
                                                .originalFileName(
                                                        attachedFile.getOriginalFileName())
                                                .fileType(attachedFile.getFileType())
                                                .filePath(attachedFile.getFilePath())
                                                .fileSize(attachedFile.getFileSize())
                                                .build())
                        .collect(Collectors.toList());

        return attachedFiles;
    }

    // 게시글 첨부파일 조회
    @Override
    public List<BoardFileResponseDto> getBoardFiles(Long boardId) {
        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(() -> BoardCustomException.BOARD_NOT_FOUND);
        board.verification();

        List<BoardAttachedFile> fileList = attachedFileRepository.findAllByBoard(board);
        List<BoardFileResponseDto> result = new ArrayList<>();
        if (!fileList.isEmpty()) {
            result =
                    fileList.stream()
                            .map(
                                    file ->
                                            BoardFileResponseDto.of(
                                                    file.getId(),
                                                    file.getStoreFileName(),
                                                    file.getOriginalFileName(),
                                                    file.getFilePath(),
                                                    file.getFileType()))
                            .collect(Collectors.toList());
        }

        return result;
    }

    // 게시글 첨부파일 삭제
    @Override
    public void deleteAttachedFiles(List<BoardAttachedFile> files) {
        for (BoardAttachedFile file : files) {
            // 실제 경로에서 파일 삭제
            fileUtils.deleteFile(file.getFilePath());
        }
    }

    // 게시글 첨부파일 수정
    @Override
    public void modifyAttachedFiles(
            Long boardId, List<MultipartFile> files, List<Long> deleteFileIds) {
        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(() -> BoardCustomException.BOARD_NOT_FOUND);

        // 삭제할 첨부파일의 값이 있을 경우
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            // 삭제할 파일목록 조회
            List<BoardAttachedFile> deleteFiles = attachedFileRepository.findAllById(deleteFileIds);

            // 게시글 첨부파일 목록에서 삭제
            for (BoardAttachedFile file : deleteFiles) {
                board.removeAttachedFile(file);
            }

            // 실제 경로에서 삭제
            deleteAttachedFiles(deleteFiles);
        }

        // 새로 추가한 첨부파일이 있을 경우
        if (files != null && !files.isEmpty()) {
            // 실제 경로에 첨부파일 추가 및 추가된 목록 조회
            List<BoardAttachedFile> addFiles = uploadAttachedFiles(board, files);

            // 게시글 첨부파일 목록에 추가
            for (BoardAttachedFile file : addFiles) {
                board.addAttachedFile(file);
            }
        }
    }
}
