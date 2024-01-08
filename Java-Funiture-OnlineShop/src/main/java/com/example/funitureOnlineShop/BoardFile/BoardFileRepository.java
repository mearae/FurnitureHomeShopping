package com.example.funitureOnlineShop.boardFile;

import com.example.funitureOnlineShop.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findByBoardId(Long boardId);
    List<BoardFile> findByBoard(Board board);
}
