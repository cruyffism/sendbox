package com.zerock.sendbox.controller.admin;

import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.entity.OwnerMember;
import com.zerock.sendbox.entity.UserMember;
import com.zerock.sendbox.service.admin.AdminManagementService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminManagementController {
    @Autowired
    AdminManagementService adminManagementService;

    //빈곽 유저 리스트 조회
    @GetMapping("/userList")
    public String userList() {
        return "admin/member/userList";
    }

    //admin의 모든 유저 리스트 조회
    @GetMapping("/userListAjax")
    public String userListAjax(Model model, @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // 화면상에선 1부터 시작하지만 자바는 0부터 시작해서 1-1= 0이고 이게 실제 페이지론 1 페이지
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort()); //위에 page 변수에 넣은 값을 다시 pageable에 할당
        List<UserMember> userList = adminManagementService.findAllByDeleteYn("N"); // 매개변수를 보내고 리턴값을 받고 이 리턴값을 왼쪽에 저장
        int start = (int) pageable.getOffset(); // 유저명단의 첫 행 >> 현재 페이지의 오프셋(시작 인덱스)을 반환 후, 이를 정수로 캐스팅하여 start 변수에 저장합니다.
        int end = Math.min((start + pageable.getPageSize()), userList.size()); // 유저명단의 마지막 행, 두개 중 최소값을 취함 >> 한 페이지에 장바구니 담긴거의 개수를 표시하려고 하는 거

        List<UserMember> pageContent = userList.subList(start, end); // start 인덱스부터 end-1 인덱스까지의 항목을 포함합니다. 따라서 pageContent에는 현재 페이지에 해당하는 항목들이 포함되어 있습니다.(size를 10으로 제한해서 최대 10까지 나옴)
        Page<UserMember> userMember = new PageImpl<>(pageContent, pageable, userList.size()); // 현재 페이지 항목, pageable, 장바구니에 담긴 개수를 userMember에 담는다 !

        model.addAttribute("userMember", userMember); //페이지로 감싼 userMember가 아래 프론트로 간다.

        return "admin/member/userListAjax";
    }

    //admin의 유저 단건 삭제
    @PostMapping("/deleteUser/{userNo}") // 중괄호 안에 변수를 @PathVariable로 받는다.// 중괄호 안에 변수를 @PathVariable로 받는다.
    public String deleteUser(@PathVariable(value = "userNo") Integer userNo, HttpServletResponse response) throws IOException {
        Integer userMember = adminManagementService.findByDeleteYn(userNo); // 리스트 타입의 변수를 보내서 인티저 타입으로 리턴!

        if (userMember != 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('삭제 되었습니다.');</script>");
            writer.flush();
            return "admin/member/userList";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('삭제에 실패 하였습니다.');</script>");
            writer.flush();
            return "admin/member/userList";
        }
    }

    //빈곽 오너 리스트 조회
    @GetMapping("/ownerList")
    public String ownerList() {
        return "admin/member/ownerList";
    }

    //admin의 모든 오너 리스트 조회
    @GetMapping("/ownerListAjax")
    public String ownerListAjax(Model model, @PageableDefault(size = 10, sort = "regDate", direction = Sort.Direction.ASC) Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<OwnerMember> ownerList = adminManagementService.findByDeleteYn("N");
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), ownerList.size());

        List<OwnerMember> pageContent = ownerList.subList(start, end);
        Page<OwnerMember> ownerMember = new PageImpl<>(pageContent, pageable, ownerList.size());

        model.addAttribute("ownerMember", ownerMember);

        return "admin/member/ownerListAjax";
    }

    //admin의 오너 단건 삭제
    @PostMapping("/deleteOwner/{ownerNo}")
    public String deleteOwner(@PathVariable(value = "ownerNo") Integer ownerNo, HttpServletResponse response) throws IOException {
        Integer ownerMember = adminManagementService.deleteOwner(ownerNo);

        if (ownerMember != 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('삭제 되었습니다.');</script>");
            writer.flush();
            return "admin/member/ownerList";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('삭제에 실패 하였습니다.');</script>");
            writer.flush();
            return "admin/member/ownerList";
        }

    }


    //빈곽 매니저 리스트 조회
    @GetMapping("/managerList")
    public String managerList() {
        return "admin/member/managerList";
    }

    //모든 매니저 리스트 조회
    @GetMapping("/managerListAjax")
    public String managerListAjax(Model model, @PageableDefault(size = 10, sort = "regDate", direction = Sort.Direction.ASC) Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<AdminMember> adminList = adminManagementService.findManager("N");
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), adminList.size());

        List<AdminMember> pageContent = adminList.subList(start, end);
        Page<AdminMember> adminMember = new PageImpl<>(pageContent, pageable, adminList.size());

        model.addAttribute("adminMember", adminMember);

        return "admin/member/managerListAjax";
    }

    //매니저 승인
    @GetMapping("/managerGrant/{adminNo}")
    public String managerGrant(@PathVariable Integer adminNo, HttpServletResponse response) throws IOException {
        Integer adminMember = adminManagementService.saveGrant(adminNo);

        if (adminMember > 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('승인이 완료 되었습니다.');</script>");
            writer.flush();
            return "admin/member/managerList";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('승인에 실패 하였습니다.');</script>");
            writer.flush();
            return "admin/member/managerList";
        }
    }

    //오너 승인
    @GetMapping("/ownerGrant/{ownerNo}")
    public String ownerGrant(@PathVariable Integer ownerNo,HttpServletResponse response) throws IOException {
        Integer ownerMember = adminManagementService.saveApproval(ownerNo);

        if (ownerMember > 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('승인이 완료 되었습니다.');</script>");
            writer.flush();
            return "admin/member/ownerList";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('승인에 실패 하였습니다.');</script>");
            writer.flush();
            return "admin/member/ownerList";
        }
    }

}
