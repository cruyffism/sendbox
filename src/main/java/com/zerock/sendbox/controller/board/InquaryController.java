package com.zerock.sendbox.controller.board;

import com.zerock.sendbox.dto.board.InquaryDTO;
import com.zerock.sendbox.dto.board.PageRequestDTO2;
import com.zerock.sendbox.service.board.InquaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inquary")
@Log4j2
@RequiredArgsConstructor
public class InquaryController {

    @Autowired
    InquaryService inquaryService;

    @GetMapping("/list")
    public void list(PageRequestDTO2 pageRequestDTO2, Model model) {
        log.info("list............" + pageRequestDTO2);

        model.addAttribute("result", inquaryService.getList(pageRequestDTO2));
    }

    @GetMapping("/register")
    public void register() {
        log.info("register get...");
    }

    @PostMapping("/register")
    public String registerPost(InquaryDTO dto, RedirectAttributes redirectAttributes) {
        log.info("dto..." + dto);

        Integer inquaryNo = inquaryService.register(dto);

        log.info("inquaryNo: " + inquaryNo);

        redirectAttributes.addFlashAttribute("msg", inquaryNo);

        return "redirect:/inquary/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(@ModelAttribute("pageRequestDTO2") PageRequestDTO2 pageRequestDTO2, Integer inquaryNo, Model model) {
        log.info("inquaryNo: " + inquaryNo);

        InquaryDTO inquaryDTO = inquaryService.get(inquaryNo);

        log.info(inquaryNo);

        model.addAttribute("dto", inquaryDTO);
    }

    @GetMapping("/remove")
    public String remove(Integer inquaryNo, RedirectAttributes redirectAttributes) {
        log.info("inquaryNo: " + inquaryNo);

        inquaryService.removeWithReplies(inquaryNo);

        redirectAttributes.addFlashAttribute("msg", inquaryNo);

        return "redirect:/inquary/list";
    }

    @PostMapping("/modify")
    public String modify(InquaryDTO dto, @ModelAttribute("pageRequestDTO2") PageRequestDTO2 pageRequestDTO2, RedirectAttributes redirectAttributes) {
        log.info("post modify.............................");
        log.info("dto: " + dto);

        inquaryService.modify(dto);

        redirectAttributes.addAttribute("page", pageRequestDTO2.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO2.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO2.getKeyword());

        redirectAttributes.addAttribute("inquaryNo", dto.getInquaryNo());

        return "redirect:/inquary/read";
    }

}