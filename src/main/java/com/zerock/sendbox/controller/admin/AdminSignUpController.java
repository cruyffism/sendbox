package com.zerock.sendbox.controller.admin;

import com.zerock.sendbox.dto.admin.SignUpDTO;
import com.zerock.sendbox.entity.MemberRole;
import com.zerock.sendbox.service.admin.SignUpService;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminSignUpController {

    private final SignUpService signUpService;

    @GetMapping("/create_account_form")
    public String createAdminMember() {
        log.info("sign..........");

        return "admin/member/create_account_form";
    }

    @PostMapping("/create_account_form")
    public String createAdminMember(SignUpDTO signUpDTO, MemberRole memberRole, HttpServletResponse response) throws IOException {
        if(signUpService.isIdDuplicated(signUpDTO.getAdminId())) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('이미 사용 중인 아이디입니다.');</script>");
            writer.flush();
            return "admin/member/create_account_form";
        } else {
            Integer roleId = memberRole.getRoleId();
            roleId = signUpDTO.getAdminId().equals("superAdmin") ? 1 : roleId;
            memberRole.setRoleId(roleId);
            int approval = signUpDTO.getAdminId().equals("superAdmin") ? 1 : 0;
            signUpDTO.setApproval(approval);
            signUpService.join(signUpDTO, memberRole);
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('회원가입이 완료되었습니다.');</script>");
            writer.flush();
            return "admin/member/login_form";
        }
    }
}
