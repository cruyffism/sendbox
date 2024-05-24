package com.zerock.sendbox.controller.user;

import com.zerock.sendbox.dto.user.UserLoginDTO;
import com.zerock.sendbox.entity.UserMember;
import com.zerock.sendbox.service.user.UserLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserLoginController {

    private final UserLoginService userLoginService;


    // 로그인 페이지 >> 페이지만 이동함
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "/user/member/login_form";
    }

    @GetMapping("/reset_password_form")
    public String resetPassword() {
        return "user/member/reset_password_form";
    }

    @PostMapping("/reset_password_form")
    public String resetPassword(@RequestParam("userId") String userId,
                                @RequestParam("mail") String mail,
                                HttpServletResponse response) throws IOException {
        UserMember userMember = userLoginService.findByUserIdAndMail(userId, mail);

        if (userMember != null) {
            userLoginService.sendPasswordResetMail(mail);
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('비밀번호 재설정 이메일이 전송되었습니다.');</script>");
            writer.flush();
            return "user/member/login_form";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('아이디 또는 이메일 주소가 올바르지 않습니다.');</script>");
            writer.flush();
            return "user/member/reset_password_form";
        }
    }

    @GetMapping("/forgot_id")
    public String showForgotIdForm() {
        return "/user/member/forgot_id_form";
    }

    @PostMapping("/forgot_id")
    public String processForgotIdForm(@RequestParam("name") String name,@RequestParam("mail") String mail,HttpServletResponse response) throws IOException {
        UserMember userMember = userLoginService.findByNameAndMail(name,mail);

        if (userMember != null) {
            String userId = userMember.getUserId();
            String subject = "Your ID Recovery";
            String text = "Your ID is: " + userId;
            userLoginService.sendMail(mail, subject, text);
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('가입하신 이메일 주소로 아이디를 전송했습니다.');</script>");
            writer.flush();
            return "user/member/login_form";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('해당 이메일 주소와 연결된 사용자가 없습니다.');</script>");
            writer.flush();
            return "user/member/forgot_id_form";
        }
    }

}
