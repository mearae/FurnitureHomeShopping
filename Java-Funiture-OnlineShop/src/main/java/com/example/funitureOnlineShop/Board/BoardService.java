package com.example.funitureOnlineShop.board;

import com.example.funitureOnlineShop.boardFile.BoardFile;
import com.example.funitureOnlineShop.boardFile.BoardFileRepository;
import com.example.funitureOnlineShop.core.error.exception.Exception403;
import com.example.funitureOnlineShop.core.error.exception.Exception500;
import com.example.funitureOnlineShop.user.User;
import com.example.funitureOnlineShop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final UserRepository userRepository;
    private final String filePath = "C:/Users/Ahyun/OneDrive/바탕 화면/카카오 로그인/수정본";

    public Page<BoardDTO> paging(Pageable pageable) {

        // ** 페이지 시작 번호
        int page = pageable.getPageNumber() - 1;

        // ** 페이지에 포함될 게시물 개수
        int size = 10;

        Page<Board> boards = boardRepository.findAll(
                PageRequest.of(page, size));
        return boards.map(board -> new BoardDTO(
                board.getId(),
                board.getUser().getId(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime()));
    }

    @Transactional
    public void save(Long userId, BoardDTO dto,
                     @RequestParam MultipartFile[] files) throws IOException {
        dto.setCreateTime(LocalDateTime.now());
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new Exception403("인증받지 않은 회원");
        User user = optionalUser.get();

        try {
            System.out.println(dto.toEntity().getId());
            Board board = boardRepository.save(dto.toEntity());
            board.updateUser(user);

            // ** 파일 정보 저장.
            if (!files[0].isEmpty()) {
                for (MultipartFile file : files) {

                    Path uploadPath = Paths.get(filePath);
                    // ** 만약 경로가 없다면... 경로 생성.
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    // ** 파일명 추출
                    String originalFileName = file.getOriginalFilename();
                    // ** 확장자 추출
                    String formatType = originalFileName.substring(
                            originalFileName.lastIndexOf("."));
                    // ** UUID 생성
                    String uuid = UUID.randomUUID().toString();
                    // ** 경로 지정
                    String path = filePath + uuid + originalFileName;
                    // ** 경로에 파일을 저장.  DB 아님
                    file.transferTo(new File(path));

                    BoardFile boardFile = BoardFile.builder()
                            .filePath(filePath)
                            .fileName(originalFileName)
                            .uuid(uuid)
                            .fileType(formatType)
                            .fileSize(file.getSize())
                            .board(board)
                            .build();
                    boardFileRepository.save(boardFile);
                }
            }
        } catch (Exception e) {
            throw new Exception500("게시판 저장 중 에러");
        }
    }

    public BoardDTO findById(Long id) {
        Board board = boardRepository.findById(id).get();
        return BoardDTO.toBoardDTO(board);
    }

    public List<BoardFile> findByBoardId(Long boardId) {
        List<BoardFile> boardFiles = boardFileRepository.findByBoardId(boardId);
        return boardFiles;
    }

    @Transactional
    public void update(BoardDTO boardDTO, MultipartFile[] files) throws IOException {
        Optional<Board> boardOptional = boardRepository.findById(boardDTO.getId());

        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();

            board.updateFromDTO(boardDTO);

            List<BoardFile> existingFiles = boardFileRepository.findByBoard(board);
            for (BoardFile file : existingFiles) {
                boardFileRepository.deleteById(file.getId());
            }

            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    Path uploadPath = Paths.get(filePath);

                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    String originalFileName = file.getOriginalFilename();
                    String formatType = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String uuid = UUID.randomUUID().toString();
                    String path = filePath + uuid + originalFileName;
                    file.transferTo(new File(path));

                    BoardFile boardFile = BoardFile.builder()
                            .filePath(filePath)
                            .fileName(originalFileName)
                            .uuid(uuid)
                            .fileType(formatType)
                            .fileSize(file.getSize())
                            .board(board)
                            .build();

                    boardFileRepository.save(boardFile);
                }
            }

            boardRepository.save(board);
        }
    }

    @Transactional
    public void deleteByBoardFile(Long id) {
        boardFileRepository.deleteById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }
}
