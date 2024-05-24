package com.zerock.sendbox.controller.board;

import com.zerock.sendbox.dto.board.BoardDTO;
import com.zerock.sendbox.dto.board.PageRequestDTO;
import com.zerock.sendbox.service.board.BoardService;
import com.zerock.sendbox.service.owner.OwnerMypageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    @Autowired
    OwnerMypageService ownerMypageService;

    @Autowired
    BoardService boardService;

    @GetMapping("/noticeList")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("list................" + pageRequestDTO);

        model.addAttribute("result", boardService.getList(pageRequestDTO));
    }


    @GetMapping("/register")
    public void register() {
        log.info("register get...");
    }

    @PostMapping("/register")
    public String registerPost(@RequestParam("title") String title, @RequestParam("content") String content,
                               RedirectAttributes redirectAttributes, @RequestParam("thumbnail") MultipartFile file) {

        BoardDTO dto = new BoardDTO();
        dto.setTitle(title);
        dto.setContent(content);

        if (!file.isEmpty()) {
            String thumbnail = ownerMypageService.uploadFile(file);
            dto.setThumbnail(thumbnail);
        }

        //새로 추가된 엔티티의 번호
        Integer boardNo = boardService.register(dto);

        log.info("boardNo: " + boardNo);

        redirectAttributes.addFlashAttribute("msg", boardNo);

        return "redirect:/board/noticeList";
    }

    @GetMapping({"/read", "/modify"})
    public void read(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Integer boardNo, Model model) {
        log.info("boardNo: " + boardNo);

        BoardDTO boardDTO = boardService.get(boardNo);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @GetMapping("/remove")
    public String remove(@RequestParam("boardNo") Integer boardNo, RedirectAttributes redirectAttributes) {
        log.info("boardNo" + boardNo);

        boardService.removeWithAdminAnswer(boardNo);

        redirectAttributes.addFlashAttribute("msg", boardNo);

        return "redirect:/board/noticeList";
    }

    @PostMapping("/modify")
    public String modify(@RequestParam("boardNo") Integer boardNo, @RequestParam("title") String title, @RequestParam("content") String content,
                         @RequestParam("thumbnail") MultipartFile file, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
        log.info("post modify...................");
        BoardDTO dto = new BoardDTO();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setBoardNo(boardNo);

        if (!file.isEmpty()) {
            String thumbnail = ownerMypageService.uploadFile(file);
            dto.setThumbnail(thumbnail);
        }

        boardService.modify(dto);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());

        redirectAttributes.addAttribute("boardNo", dto.getBoardNo());

        return "redirect:/board/read";
    }


}
