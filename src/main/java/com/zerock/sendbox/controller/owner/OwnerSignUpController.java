package com.zerock.sendbox.controller.owner;

import com.zerock.sendbox.dto.owner.OwnerSignUpDTO;
import com.zerock.sendbox.entity.MemberRole;
import com.zerock.sendbox.entity.OwnerMember;
import com.zerock.sendbox.entity.Room;
import com.zerock.sendbox.entity.Store;
import com.zerock.sendbox.service.owner.OwnerMypageService;
import com.zerock.sendbox.service.owner.OwnerSignUpService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
@Log4j2
public class OwnerSignUpController {

    private final OwnerMypageService ownerMypageService;

    private final OwnerSignUpService ownerSignUpService;

    @GetMapping("/create_account_form")
    public String createOwnerMember() {
        log.info("sign.......");

        return "owner/member/create_account_form";
    }

    @PostMapping("/create_account_form")
    public String createOwnerMember(OwnerSignUpDTO signUpDTO, MemberRole memberRole, HttpServletResponse response, Model model) throws IOException {
        if(ownerSignUpService.isIdDuplicated(signUpDTO.getOwnerId())) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('이미 사용 중인 아이디입니다.');</script>");
            writer.flush();
            return "owner/member/create_account_form";
        } else {
            Integer ownerNo = ownerSignUpService.join(signUpDTO, memberRole);
            model.addAttribute("ownerNo", ownerNo);
            return "owner/member/storeRegisterForm";
        }
    }

    //업체 등록하기
    @PostMapping("/storeInfo")
    public String storeInfo(Model model, HttpServletResponse response,
                                  @RequestParam(value = "ownerNo") Integer ownerNo,
                                  @RequestParam(value = "storeName") String storeName,
                                  @RequestParam(value = "notice") String notice,
                                  @RequestParam(value = "region") String region,
                                  @RequestParam(value = "address") String address,
                                  @RequestParam(value = "brn") String brn,
                                  @RequestParam(value = "phone") String phone,
                                  @RequestParam(value = "thumbnail") MultipartFile thumbnail,
                                  @RequestParam(value = "infoPhoto") MultipartFile infoPhoto,
                                  @RequestParam(value = "size1") String size1,
                                  @RequestParam(value = "size2") String size2,
                                  @RequestParam(value = "size3") String size3,
                                  @RequestParam(value = "price1") Integer price1,
                                  @RequestParam(value = "price2") Integer price2,
                                  @RequestParam(value = "price3") Integer price3,
                                  @RequestParam(value = "remain1") Integer remain1,
                                  @RequestParam(value = "remain2") Integer remain2,
                                  @RequestParam(value = "remain3") Integer remain3) throws IOException {
        OwnerMember ownerMember = new OwnerMember();
        ownerMember.setOwnerNo(ownerNo);

        Store storeInfo = new Store();
        storeInfo.setStoreName(storeName);
        storeInfo.setNotice(notice);
        storeInfo.setRegion(region);
        storeInfo.setAddress(address);
        storeInfo.setBrn(brn);
        storeInfo.setPhone(phone);
        storeInfo.setOwnerMember(ownerMember);


        // 서비스
        String thumbnailPath = ownerMypageService.uploadFile(thumbnail);
        storeInfo.setThumbnail(thumbnailPath);

        // 서비스
        String infoPhotoPath = ownerMypageService.uploadFile(infoPhoto);
        storeInfo.setInfoPhoto(infoPhotoPath);


        Store result = ownerMypageService.save(storeInfo);
        // 업체정보 등록이 완료되면 탈퇴여부를 N으로 바꿔서 로그인이 가능하게 처리
        OwnerMember byOwnerNo = ownerMypageService.findByOwnerNo(ownerNo);
        byOwnerNo.setDeleteYn("N");
        OwnerMember result2 =  ownerMypageService.ownerSave(byOwnerNo);

        List<Room> roomList = new ArrayList<>();

        Room room1 = new Room();
        room1.setSize(size1);
        room1.setPrice(price1);
        room1.setRemain(remain1);
        room1.setStore(result);

        Room room2 = new Room();
        room2.setSize(size2);
        room2.setPrice(price2);
        room2.setRemain(remain2);
        room2.setStore(result);

        Room room3 = new Room();
        room3.setSize(size3);
        room3.setPrice(price3);
        room3.setRemain(remain3);
        room3.setStore(result);

        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room3);


        List<Room> roomUpdateInfo = ownerMypageService.saveAll(roomList); // 룸 리스트 정보 저장

        if (result != null && roomUpdateInfo.size() != 0) { // 성공 시 업데이트 정보 보내기
            model.addAttribute("storeInfo", result);
            model.addAttribute("roomList", roomUpdateInfo);
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('회원가입에 성공하였습니다.');</script>");
            writer.flush();
            return "owner/member/login_form";
        } else {                                          // 실패시 기존 정보 보내기
            model.addAttribute("storeInfo", storeInfo);
            model.addAttribute("roomList", roomUpdateInfo);
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('회원가입에 실패하였습니다.');</script>");
            writer.flush();
            return "owner/member/storeRegisterForm";
        }

    }
}