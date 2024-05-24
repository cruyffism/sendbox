package com.zerock.sendbox.controller.owner;

import com.zerock.sendbox.dto.payment.ApproveResDto;
import com.zerock.sendbox.dto.payment.CancelReqDto;
import com.zerock.sendbox.dto.payment.CancelResDto;
import com.zerock.sendbox.entity.*;
import com.zerock.sendbox.service.owner.OwnerMypageService;
import com.zerock.sendbox.service.user.PaymentService;
import com.zerock.sendbox.service.user.UserMypageService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/owner")
public class OwnerMypageController {
    @Autowired
    OwnerMypageService ownerMypageService;

    @Autowired
    UserMypageService userMypageService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    PasswordEncoder passwordEncoder;

    //오너 정보 수정폼 >> 빈곽
    @GetMapping("/mypageForm")
    public String mypageForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String ownerId = auth.getName();

        OwnerMember ownerMember = ownerMypageService.findByOwnerId(ownerId); //OwnerMember 타입으로 결과값을 받는다.
        model.addAttribute("owner", ownerMember);
        return "owner/member/modify_account_form";
    }

    //오너 정보 수정
    @PostMapping("/updateInfo")
    public String updateInfo(@ModelAttribute OwnerMember ownerMember, @ModelAttribute MemberRole memberRole) { // 프론트에서 "오너 정보 수정 완료" 버튼을 누르면 그 값을 @ModelAttribute로 받으면 된다.
        //비밀번호 암호화
        ownerMember.setPassword(passwordEncoder.encode(ownerMember.getPassword()));
        ownerMember.setMemberRole(memberRole);
        OwnerMember result = ownerMypageService.updateInfo(ownerMember);
        if (result != null) {
            return "owner/member/modify_account_ok";
        } else {
            return "owner/member/modify_account_ng";
        }

    }

    //오너 정보 탈퇴
    @PostMapping("/deleteInfo")
    public String deleteInfo(@ModelAttribute OwnerMember ownerMember, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String ownerId = auth.getName();

        OwnerMember owner = ownerMypageService.findByOwnerId(ownerId);

        if (passwordEncoder.matches(ownerMember.getPassword(), owner.getPassword())) { // 실제 입력한 비번, DB에 비번 비교
            Integer result = ownerMypageService.deleteInfo(owner.getOwnerNo());
            session.invalidate();
            return "redirect:/owner/home";
        } else {
            model.addAttribute("owner", owner); // 비번 틀렸을때 다시 modify_account_form 프론트 화면으로 가야하니까 값을 뿌려준다.
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('비밀번호가 틀렸습니다.');</script>");
            writer.flush();
            return "owner/member/modify_account_form"; // 원래 리다이렉트를 하면 model.~ 안해도 되지만 alert창과 redirect 같이 사용이 안됨!
        }

    }

    //빈곽 예약 내역 조회
    @GetMapping("/reservationList")
    public String reservationForm() {
        return "owner/member/reservationList";
    }

    //사업자의 예약 내역 리스트 조회
    @GetMapping("/reservationListAjax")
    public String reservationList(Model model, @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String ownerId = auth.getName();
        OwnerMember owner = ownerMypageService.findByOwnerId(ownerId);
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); //화면상에선 1부터 시작하지만 자바는 0부터 시작해서 1-1= 0이고 이게 실제 페이지론 1 페이지
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort()); //위에 page 변수에 넣은 값을 다시 pageable에 할당
        List<Orders> reservationList = ownerMypageService.findAllUserReservation(owner.getOwnerNo()); // 매개변수를 보내고 리턴값을 받고 이 리턴값을 왼쪽에 저장
        int start = (int) pageable.getOffset(); // 예약리스트의 첫 행 >> 현재 페이지의 오프셋(시작 인덱스)을 반환 후, 이를 정수로 캐스팅하여 start 변수에 저장합니다.
        int end = Math.min((start + pageable.getPageSize()), reservationList.size()); // 예약리스트의 마지막 행, 두개 중 최소값을 취함 >> 한 페이지에 예약리스트에 담긴거의 개수를 표시하려고 하는 거

        List<Orders> pageContent = reservationList.subList(start, end); // start 인덱스부터 end-1 인덱스까지의 항목을 포함합니다. 따라서 pageContent에는 현재 페이지에 해당하는 항목들이 포함되어 있습니다.(size를 10으로 제한해서 최대 10까지 나옴)
        Page<Orders> orders = new PageImpl<>(pageContent, pageable, reservationList.size()); // 현재 페이지 항목, pageable, 예약리스트에 담긴 개수를 orders에 담는다 !
        model.addAttribute("reservations", orders);
        return "owner/member/reservationListAjax";

    }

    //매장 정보 수정폼 조회
    @GetMapping("/storeForm")
    public String storeForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String ownerId = auth.getName();
        OwnerMember owner = ownerMypageService.findByOwnerId(ownerId);
        // 스토어 정보만
        Store store = ownerMypageService.findByInfoOwnerNo(owner.getOwnerNo());
        // 룸리스트 정보
        List<Room> roomList = userMypageService.findAllRoomList(store.getStoreNo());

        model.addAttribute("storeInfo", store);
        model.addAttribute("roomList", roomList);
        return "owner/member/storeForm";

    }


    //매장 정보 수정
    @PostMapping("/updateStoreInfo")
    public String updateStoreInfo(Model model, HttpServletResponse response,
                                  @RequestParam(value = "storeNo", required = false) Integer storeNo,
                                  @RequestParam(value = "notice", required = false) String notice,
                                  @RequestParam(value = "region", required = false) String region,
                                  @RequestParam(value = "address", required = false) String address,
                                  @RequestParam(value = "phone", required = false) String phone,
                                  @RequestParam(value = "brn", required = false) String brn,
                                  @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                                  @RequestParam(value = "infoPhoto", required = false) MultipartFile infoPhoto,
                                  @RequestParam(value = "roomNo1", required = false) Integer roomNo1,
                                  @RequestParam(value = "roomNo2", required = false) Integer roomNo2,
                                  @RequestParam(value = "roomNo3", required = false) Integer roomNo3,
                                  @RequestParam(value = "size1", required = false) String size1,
                                  @RequestParam(value = "size2", required = false) String size2,
                                  @RequestParam(value = "size3", required = false) String size3,
                                  @RequestParam(value = "price1", required = false) Integer price1,
                                  @RequestParam(value = "price2", required = false) Integer price2,
                                  @RequestParam(value = "price3", required = false) Integer price3,
                                  @RequestParam(value = "remain1", required = false) Integer remain1,
                                  @RequestParam(value = "remain2", required = false) Integer remain2,
                                  @RequestParam(value = "remain3", required = false) Integer remain3) throws IOException {
        Store storeInfo = ownerMypageService.findByStoreNo(storeNo);
        Store storeUpdateInfo = storeInfo;
        storeUpdateInfo.setNotice(notice);
        storeUpdateInfo.setRegion(region);
        storeUpdateInfo.setAddress(address);
        storeUpdateInfo.setPhone(phone);
        storeUpdateInfo.setBrn(brn);

        if (!thumbnail.isEmpty()) { // 빈값이 아니면 신규 사진으로 수정
            // 서비스
            String thumbnailPath = ownerMypageService.uploadFile(thumbnail);
            storeUpdateInfo.setThumbnail(thumbnailPath);
        }

        if (!infoPhoto.isEmpty()) { // 빈값이 아니면 신규 사진으로 수정
            // 서비스
            String infoPhotoPath = ownerMypageService.uploadFile(infoPhoto);
            storeUpdateInfo.setInfoPhoto(infoPhotoPath);
        }

        Store result = ownerMypageService.save(storeUpdateInfo);

        List<Room> roomList = new ArrayList<>();

        Room room1 = new Room();
        room1.setRoomNo(roomNo1);
        room1.setSize(size1);
        room1.setPrice(price1);
        room1.setRemain(remain1);
        room1.setStore(storeUpdateInfo);

        Room room2 = new Room();
        room2.setRoomNo(roomNo2);
        room2.setSize(size2);
        room2.setPrice(price2);
        room2.setRemain(remain2);
        room2.setStore(storeUpdateInfo);

        Room room3 = new Room();
        room3.setRoomNo(roomNo3);
        room3.setSize(size3);
        room3.setPrice(price3);
        room3.setRemain(remain3);
        room3.setStore(storeUpdateInfo);

        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room3);

        List<Room> roomUpdateInfo = ownerMypageService.saveAll(roomList); // 룸 리스트 정보 저장

        if (result != null && roomUpdateInfo.size() != 0) { // 성공 시 업데이트 정보 보내기
            model.addAttribute("storeInfo", result);
            model.addAttribute("roomList", roomUpdateInfo);
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('수정 완료입니다.');</script>");
            writer.flush();
            return "owner/member/storeForm";
        } else {                                          // 실패시 기존 정보 보내기
            model.addAttribute("storeInfo", storeInfo);
            model.addAttribute("roomList", roomUpdateInfo);
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('수정 실패입니다.');</script>");
            writer.flush();
            return "owner/member/storeForm";
        }

    }
    //결제 취소 api
    @PostMapping("/paymentCancel/{orderNo}")
    public String paymentCancel(@PathVariable(value = "orderNo") Integer orderNo, HttpServletResponse response) throws IOException {
        
        //tid 및 가격 찾기 위한 메서드
        Orders orders =paymentService.findPaymentInfo(orderNo);

        //취소할 정보 저장
        CancelReqDto cancelReqDto = new CancelReqDto();
        cancelReqDto.setCid("TC0ONETIME");
        cancelReqDto.setTid(orders.getPayment().getTId());
        cancelReqDto.setCancel_amount(String.valueOf(orders.getTotalPrice()));
        cancelReqDto.setCancel_tax_free_amount("0");

        // 결제 환불 api 호출
        CancelResDto cancelResDto = paymentService.paymentCancel(cancelReqDto); // cancelReqDto를 변수로 보내서 리턴을 cancelResDto로 받는다

        Integer result = paymentService.paymentCancel(orderNo);

        if (result != 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('취소 되었습니다.');</script>");
            writer.flush();
            return "owner/member/reservationList";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('취소 실패 하였습니다.');</script>");
            writer.flush();
            return "owner/member/reservationList"; // 원래 리다이렉트를 하면 model.~ 안해도 되지만 alert창과 redirect 같이 사용이 안됨!
        }
    }

}


