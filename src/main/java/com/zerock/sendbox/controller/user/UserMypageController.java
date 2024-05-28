package com.zerock.sendbox.controller.user;

import com.zerock.sendbox.entity.*;
import com.zerock.sendbox.service.store.StoreService;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserMypageController {
    @Autowired
    UserMypageService userMypageService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    StoreService storeService;

    //개인 정보 수정폼  >> 단순 화면 조회
    @GetMapping("/mypageForm")
    public String mypageForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String userId = auth.getName();

        UserMember userMember = userMypageService.findByUserId(userId); //UserMember 타입으로 결과값을 받는다.
        model.addAttribute("user", userMember); // userMember를 "user"에 담아서 프론트로 보내준다.(화면에 엔터티에 담겨진 데이터를 동적으로 뿌려주려고)

        return "user/member/modify_account_form";
    }

    //개인 정보 수정
    @PostMapping("/updateInfo")

    public String updateInfo(@ModelAttribute UserMember userMember, @ModelAttribute MemberRole memberRole) { // 프론트에서 "개인 정보 수정 완료" 버튼을 누르면 그 값을 @ModelAttribute로 받으면 된다.
        //비밀번호 암호화
        userMember.setPassword(passwordEncoder.encode(userMember.getPassword()));
        userMember.setMemberRole(memberRole);
        UserMember result = userMypageService.updateInfo(userMember);
        if (result != null) {
            return "user/member/modify_account_ok";
        } else {
            return "user/member/modify_account_ng";
        }
    }

    //개인 정보 탈퇴
    @PostMapping("/deleteInfo")
    public String deleteInfo(@ModelAttribute UserMember userMember, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String userId = auth.getName();
        UserMember user = userMypageService.findByUserId(userId);

        if (passwordEncoder.matches(userMember.getPassword(), user.getPassword())) { // 실제 입력한 비번, DB에 비번 비교
            Integer result = userMypageService.deleteInfo(user.getUserNo());
            session.invalidate();
            return "redirect:/";
        } else {
            model.addAttribute("user", user); // 비번 틀렸을때 다시 modify_account_form 프론트 화면으로 가야하니까 값을 뿌려준다.
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('비밀번호가 틀렸습니다.');</script>");
            writer.flush();
            return "user/member/modify_account_form"; // 원래 리다이렉트를 하면 model.~ 안해도 되지만 alert창과 redirect 같이 사용이 안됨!
        }

    }

    //빈곽 예약 내역 조회
    @GetMapping("/reservationList")
    public String reservationForm() {
        return "user/member/reservationList";
    }

    //유저의 예약 내역 리스트 조회
    @GetMapping("/reservationListAjax")
    public String reservationList(Model model, @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String userId = auth.getName();
        UserMember user = userMypageService.findByUserId(userId);
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Orders> reservationList = userMypageService.findAllReservation(user.getUserNo());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reservationList.size());

        List<Orders> pageContent = reservationList.subList(start, end);
        Page<Orders> orders = new PageImpl<>(pageContent, pageable, reservationList.size());
        model.addAttribute("reservations", orders);

        return "user/member/reservationListAjax";
    }

    //예약 번호 클릭으로 예약 내역 조회
    @GetMapping("/findReservation")
    public String findReservation(@RequestParam("orderNo") Integer orderNo, Model model) {
        Orders orders = userMypageService.findAllByOrderNo(orderNo);
        model.addAttribute("orderInfo", orders);
        model.addAttribute("paymentInfo", orders.getPayment());

        return "user/member/paymentConfirm";
    }


    //빈곽 카트폼 조회
    @GetMapping("/cartList")
    public String cartForm() {
        return "user/member/cartList";
    }

    //장바구니 리스트 조회
    @GetMapping("/cartListAjax")
    public String cartList(Model model, @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String userId = auth.getName();
        UserMember user = userMypageService.findByUserId(userId);
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // 화면상에선 1부터 시작하지만 자바는 0부터 시작해서 1-1= 0이고 이게 실제 페이지론 1 페이지
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());//위에 page 변수에 넣은 값을 다시 pageable에 할당
        List<Orders> cartList = userMypageService.findAllCartList(user.getUserNo()); // 매개변수를 보내고 리턴값을 받고 이 리턴값을 왼쪽에 저장

        int start = (int) pageable.getOffset(); // 장바구니의 첫 행 >> 현재 페이지의 오프셋(시작 인덱스)을 반환 후, 이를 정수로 캐스팅하여 start 변수에 저장합니다.
        int end = Math.min((start + pageable.getPageSize()), cartList.size()); // 장바구니의 마지막 행, 두개 중 최소값을 취함 >> 한 페이지에 장바구니 담긴거의 개수를 표시하려고 하는 거

        List<Orders> pageContent = cartList.subList(start, end); // start 인덱스부터 end-1 인덱스까지의 항목을 포함합니다. 따라서 pageContent에는 현재 페이지에 해당하는 항목들이 포함되어 있습니다.(size를 10으로 제한해서 최대 10까지 나옴)
        Page<Orders> orders = new PageImpl<>(pageContent, pageable, cartList.size()); // 현재 페이지 항목, pageable, 장바구니에 담긴 개수를 orders에 담는다 !

        List<Room> roomList = new ArrayList<>();
        if(cartList.size() == 0){
            roomList = null;
        } else {
            // storeNo를 통해 한 업체가 갖고 있는 room 리스트를 가져옴
            roomList = userMypageService.findAllRoomList(cartList.get(0).getRoom().getStore().getStoreNo());
        }
        model.addAttribute("cartList", orders); // 페이지로 감싼 orders가 아래 프론트로 간다.
        model.addAttribute("roomList", roomList);

        return "user/member/cartListAjax";
    }

    //장바구니 단건 수정폼 조회
    @GetMapping("/cartInfoForm/{orderNo}")
    public String cartInfoForm(@PathVariable Integer orderNo, Model model) {
        Orders order = userMypageService.findByOrderNo(orderNo);
        List<Room> roomList = userMypageService.findAllRoomList(order.getRoom().getStore().getStoreNo());

        model.addAttribute("cart", order);
        model.addAttribute("roomList", roomList);
        return "user/member/cartListUpdateForm";
    }


    //장바구니 옵션 수정
    @PostMapping("/updateCart")
    public String updateCart(@ModelAttribute Orders orders, @ModelAttribute Room room, Model model, HttpServletResponse response) throws IOException {

        Orders updateInfo =  userMypageService.findByOrderNo(orders.getOrderNo());
        Room roomInfo = userMypageService.findByRoomNo(room.getRoomNo());
        Integer betweenDays = (int) Duration.between(orders.getStartDate().atStartOfDay(), orders.getEndDate().atStartOfDay()).toDays() + 1;
        // 기존 값들을 새로운 값으로 넣기위해 강제로 저장
        updateInfo.setStartDate(orders.getStartDate());
        updateInfo.setEndDate(orders.getEndDate());
        updateInfo.setRoom(room);
        updateInfo.setTotalAmount(orders.getTotalAmount());
        updateInfo.setTotalPrice(roomInfo.getPrice() * orders.getTotalAmount() * betweenDays);
        Orders result = userMypageService.save(updateInfo);

        if (result != null) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('수정이 완료 되었습니다.');</script>");
            writer.flush();
            return "user/member/cartList";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('수정에 실패 하였습니다.');</script>");
            writer.flush();
            return "user/member/cartList"; // 원래 리다이렉트를 하면 model.~ 안해도 되지만 alert창과 redirect 같이 사용이 안됨!
        }

    }
    //장바구니 단건 삭제
    @PostMapping("/deleteCart/{orderNo}") // 중괄호 안에 변수를 @PathVariable로 받는다.
    public String deleteCart(@PathVariable(value = "orderNo") Integer orderNo, HttpServletResponse response) throws IOException {
        Integer orders = userMypageService.deleteCart(orderNo);  // 리스트 타입의 변수를 보내서 인티저 타입으로 리턴!

        if (orders != 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('삭제 되었습니다.');</script>");
            writer.flush();
            return "user/member/cartList";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('삭제에 실패 하였습니다.');</script>");
            writer.flush();
            return "user/member/cartList"; // 원래 리다이렉트를 하면 model.~ 안해도 되지만 alert창과 redirect 같이 사용이 안됨!
        }
        
    }

    //장바구니 전체 삭제
    @PostMapping("/deleteAllCart/{userNo}")
    public String deleteAllCart(@PathVariable(value = "userNo") Integer userNo, HttpServletResponse response) throws IOException {
        Integer orders = userMypageService.deleteAllCart(userNo);

        if (orders != 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('삭제 되었습니다.');</script>");
            writer.flush();
            return "user/member/cartList";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('삭제에 실패 하였습니다.');</script>");
            writer.flush();
            return "user/member/cartList"; // 원래 리다이렉트를 하면 model.~ 안해도 되지만 alert창과 redirect 같이 사용이 안됨!
        }

    }

    //장바구니 담기
    @PostMapping("/addCart")
    public String addCart(@ModelAttribute Orders orders, @ModelAttribute Room room,@ModelAttribute Store store, Model model, HttpServletResponse response) throws IOException {
        Integer betweenDays = (int) Duration.between(orders.getStartDate().atStartOfDay(), orders.getEndDate().atStartOfDay()).toDays() + 1;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String userId = auth.getName();
        UserMember user = userMypageService.findByUserId(userId);
        Room roomInfo = userMypageService.findByRoomNo(room.getRoomNo());
        String parsedLocalDateTimeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 화면상에 없는 값들을 DB에 넣기 위해 강제로 저장
        orders.setUserMember(user);
        orders.setRoom(room);
        orders.setReservationNum(parsedLocalDateTimeNow);
        orders.setReservationStatus("예약대기");
        orders.setTotalPrice(roomInfo.getPrice() * orders.getTotalAmount() * betweenDays);
        Orders cartOrderInfo = userMypageService.saveOrders(orders);

        if (cartOrderInfo != null) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('장바구니 담기가 완료되었습니다.');</script>");
            writer.flush();
            Store storeDetail = storeService.getStoreDetail(store.getStoreNo());
            model.addAttribute("store", storeDetail);
            return "store/store_detail";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('장바구니 담기에 실패 하였습니다.');</script>");
            writer.flush();
            Store storeDetail = storeService.getStoreDetail(store.getStoreNo());
            model.addAttribute("store", storeDetail);
            return "store/store_detail"; // 원래 리다이렉트를 하면 model.~ 안해도 되지만 alert창과 redirect 같이 사용이 안됨!
        }

    }

}
