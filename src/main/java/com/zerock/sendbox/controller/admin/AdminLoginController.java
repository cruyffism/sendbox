package com.zerock.sendbox.controller.admin;

import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.service.admin.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminLoginController {

    private final LoginService loginService;


    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "admin/member/login_form";
    }

    //패스워드 재설정
    @GetMapping("/reset_password_form")
    public String resetPassword() {
        return "admin/member/reset_password_form";
    }

    @PostMapping("/reset_password_form")
    public String resetPassword(@RequestParam("adminId") String adminId,
                                @RequestParam("mail") String mail,
                                HttpServletResponse response) throws IOException {
        // 아이디와 이메일로 사용자를 찾습니다.
        AdminMember adminMember = loginService.findByAdminIdAndMail(adminId, mail);

        if (adminMember != null) {
            loginService.sendPasswordResetMail(mail);
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('비밀번호 재설정 이메일이 전송되었습니다.');</script>");
            writer.flush();
            return "admin/member/login_form";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('아이디 또는 이메일 주소가 올바르지 않습니다.');</script>");
            writer.flush();
            return "admin/member/reset_password_form";
        }
    }

    @GetMapping("/forgot_id")
    public String showForgotIdForm() {
        return "admin/member/forgot_id_form";
    }

    @PostMapping("/forgot_id")
    public String processForgotIdForm(@RequestParam("name") String name,@RequestParam("mail") String mail,HttpServletResponse response) throws IOException {
        // 이메일 주소를 이용하여 사용자를 찾습니다.
        AdminMember adminMember = loginService.findByNameAndMail(name,mail);

        if (adminMember != null) {
            String userId = adminMember.getAdminId();
            String subject = "Your ID Recovery";
            String text = "Your ID is: " + userId;
            loginService.sendMail(mail, subject, text);
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('가입하신 이메일 주소로 아이디를 전송했습니다.');</script>");
            writer.flush();
            return "admin/member/login_form";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('해당 이메일 주소와 연결된 사용자가 없습니다.');</script>");
            writer.flush();
            return "admin/member/forgot_id_form";
        }
    }


}