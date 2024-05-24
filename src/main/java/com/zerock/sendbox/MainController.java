package com.zerock.sendbox;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {
    //유저홈으로 이동
    @GetMapping("/")
    public String mainHome(Model model) {
        model.addAttribute("title", "");

        return "user/home";
    }

    //오너홈으로 이동
    @GetMapping("/owner/home")
    public String ownerHome(Model model) {
        model.addAttribute("title", "");

        return "owner/home";
    }

    //어드민홈으로 이동
    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        model.addAttribute("title", "");

        return "admin/home";
    }


}