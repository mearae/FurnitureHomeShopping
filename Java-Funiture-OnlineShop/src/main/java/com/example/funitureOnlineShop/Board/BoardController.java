package com.example.funitureOnlineShop.board;

import com.example.funitureOnlineShop.boardFile.BoardFile;
import com.example.funitureOnlineShop.core.error.exception.Exception500;
import com.example.funitureOnlineShop.core.security.CustomUserDetails;
import com.example.funitureOnlineShop.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/create")
    public String boardCreateForm() {
        return "boardCreate";
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                       @ModelAttribute BoardDTO requestDTO,
                       @RequestParam MultipartFile[] files) throws IOException {
        try {
            Long userId = customUserDetails.getUser().getId();
            requestDTO.setCreateTime(LocalDateTime.now());
            boardService.save(userId, requestDTO, files);
            return ResponseEntity.ok(ApiUtils.success(null));
        } catch (Exception e) {
            // 예외가 발생하면 로그에 기록하고 적절한 처리를 수행하세요.
            e.printStackTrace();
            // 예외를 다시 던져도 되고, 에러 페이지로 리다이렉트하거나 다른 적절한 조치를 취할 수 있습니다.
            throw new Exception500("글 저장 중 오류가 발생했습니다.");
        }
    }

    @GetMapping(value = {"/paging","/"})
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
        Page<BoardDTO> boards = boardService.paging(pageable);

        int blockLimit = 3;
        int startPage = (int)(Math.ceil((double)pageable.getPageNumber() / blockLimit) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), boards.getTotalPages());

        model.addAttribute("boardList", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardPage";
    }

    // CRUD update / "update" 템플릿을 렌더링하여 반환
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {

        BoardDTO boardDTO = boardService.findById(id);
        List<BoardFile> existingFiles = boardService.findByBoardId(id); // 기존 파일 목록 조회
        model.addAttribute("board", boardDTO);
        model.addAttribute("existingFiles", existingFiles);

        return "boardUpdate";
    }
    // CRUD update / "/board/"로 리다이렉트
    @PostMapping("/update")
    public String update(
                         @ModelAttribute BoardDTO boardDTO,
                         @RequestParam MultipartFile[] files) throws IOException {

        boardService.update(boardDTO,files);
        return "redirect:/board/";
    }
    // CRUD Read /"detail" 템플릿을 렌더링하여 반환
    @GetMapping("/{id}")
    public String paging(@PathVariable Long id, Model model,
                         @PageableDefault(page = 1) Pageable pageable){

        BoardDTO dto = boardService.findById(id);
        List<BoardFile> files = boardService.findByBoardId(id);

        model.addAttribute("board", dto);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("files", files != null ? files : Collections.emptyList());

        return "boardDetail";
    }

    // CRUD delete /  "/board/paging"으로 리다이렉트
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        System.out.println(id);
        boardService.deleteById(id);
        return "redirect:/board/paging";
    }

    @DeleteMapping("/deleteByBoardFile/{id}")
    public void deleteByBoardFile(@PathVariable Long id) {
        System.out.println(id);
        boardService.deleteByBoardFile(id);
    }
}