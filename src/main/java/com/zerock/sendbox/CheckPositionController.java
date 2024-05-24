package com.zerock.sendbox;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckPositionController {

    //로그인 할 때 직책을 확인하는 페이지
    @GetMapping("/checkPositionLogin")
    public String checkPositionLogin() {
        return "include/check_position_login";
    }

    //회원가입 할 때 직책을 확인하는 페이지
    @GetMapping("/checkPositionRegister")
    public String checkPositionRegister() {
        return "include/check_position_create_account";
    }

    //어드민 회원가입 약관
    @GetMapping("/admin/terms")
    public String adminterms() {

        return "admin/member/account_terms";
    }
    //오너 회원가입 약관
    @GetMapping("/owner/terms")
    public String ownerTerms() {
        return "owner/member/account_terms";
    }
    //유저 회원가입 약관
    @GetMapping("/user/terms")
    public String userTerms() {
        return "user/member/account_terms";
    }

    //개인정보 처리방침
    @GetMapping("/privacyterms")
    public String privacyTerms() {
        return "include/privacy_terms";
    }
    //이용약관
    @GetMapping("/policyterms")
    public String policyTerms() {
        return "include/policy_terms";
    }
    //저작권정책
    @GetMapping("/copyrightterms")
    public String copyRightTerms() {
        return "include/copyright_terms";
    }
    //리뷰운영정책
    @GetMapping("/reviewterms")
    public String reviewTerms() {
        return "include/review_terms";
    }
}

