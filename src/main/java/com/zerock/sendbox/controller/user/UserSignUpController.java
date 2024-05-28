package com.zerock.sendbox.controller.user;

import com.zerock.sendbox.dto.user.UserSignUpDTO;
import com.zerock.sendbox.entity.MemberRole;
import com.zerock.sendbox.service.user.UserSignUpService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserSignUpController {

    private final UserSignUpService userSignUpService;

    @GetMapping("/create_account_form")
    public String createUserMember() {
        log.info("sign.......");

        return "user/member/create_account_form";
    }

    @PostMapping("/create_account_form")
    public String createUserMember(UserSignUpDTO userSignUpDTO, MemberRole memberRole, HttpServletResponse response) throws IOException {
        if(userSignUpService.isIdDuplicated(userSignUpDTO.getUserId())) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('이미 사용 중인 아이디입니다.');</script>");
            writer.flush();
            return "user/member/create_account_form";
        } else {
            userSignUpService.join(userSignUpDTO, memberRole);
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('회원가입이 완료되었습니다.');</script>");
            writer.flush();
            return "user/member/login_form";
        }
    }
}
