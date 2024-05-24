package com.zerock.sendbox.controller.user;


import com.zerock.sendbox.dto.payment.*;
import com.zerock.sendbox.entity.*;
import com.zerock.sendbox.service.user.PaymentService;
import com.zerock.sendbox.service.user.UserMypageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/user")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @Autowired
    UserMypageService userMypageService;

    //결제창 가기
    @GetMapping("/paymentInfoForm/{orderNo}")
    public String paymentInfoForm(@PathVariable Integer orderNo, Model model) {
        Orders order = userMypageService.findByOrderNo(orderNo);

        model.addAttribute("order", order);
        return "user/member/paymentInfoForm";
    }


    //결제 요청
    @GetMapping("/payment")
    @ResponseBody                //cartList 혹은 상세페이지에서 결제 버튼 클릭 시 폼 안에 데이터를 받기 위해
    public PaymentResDto payment(@ModelAttribute Orders orders, @ModelAttribute Room room, @ModelAttribute Store store) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        UserMember user = userMypageService.findByUserId(userId);
        // 룸 금액 계산
        Room roomInfo = userMypageService.findByRoomNo(room.getRoomNo());
        Integer betweenDays = (int) Duration.between(orders.getStartDate().atStartOfDay(), orders.getEndDate().atStartOfDay()).toDays() + 1;

        // 바로 결제하기 누르면 orders 저장
        if(orders.getOrderNo() == null){
            String parsedLocalDateTimeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 화면상에 없는 값들을 DB에 넣기 위해 강제로 저장
            orders.setUserMember(user);
            orders.setRoom(room);
            orders.setReservationNum(parsedLocalDateTimeNow);
            orders.setReservationStatus("예약대기");
            orders.setTotalPrice(roomInfo.getPrice() * orders.getTotalAmount() * betweenDays);
            orders = userMypageService.saveOrders(orders);
        }

        PaymentReqDto paymentReqDto = new PaymentReqDto();
        paymentReqDto.setPartner_order_id(orders.getReservationNum());
        paymentReqDto.setPartner_user_id(user.getUserId());
        paymentReqDto.setItem_name(store.getStoreName());
        paymentReqDto.setQuantity(orders.getTotalAmount());
        paymentReqDto.setTotal_amount(roomInfo.getPrice() * orders.getTotalAmount() * betweenDays);
        paymentReqDto.setCid("TC0ONETIME");
        paymentReqDto.setTax_free_amount(0);
        paymentReqDto.setApproval_url("http://localhost:8080/user/payment/success");
        paymentReqDto.setFail_url("http://localhost:8080/user/payment/fail");
        paymentReqDto.setCancel_url("http://localhost:8080/user/cartList");

        System.out.println("paymentReqDto = " + paymentReqDto);
        PaymentResDto paymentResDto = paymentService.paymentRequest(paymentReqDto); // paymentReqDto를 변수로 보내서 리턴을 paymentResDto로 받는다

        // payment에 tid 저장
        Payment payment = new Payment();
        payment.setTId(paymentResDto.getTid());
        Payment result = paymentService.save(payment);

        // orders에 paymentId 저장
        int paymentResult =  paymentService.payment(orders.getOrderNo(), result.getPaymentNo());

        System.out.println("paymentResDto = " + paymentResDto);

        return paymentResDto;
    }

    //화면창에서 결제 성공 시 프론트에 결제 완료 html 보여주기
    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("pg_token") String pgToken, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서  글 저장하려할ㅈ때 로그인 정보 가져와서 아이디 값을 디비에 넣어주는거!
        String userId = auth.getName();
        UserMember user = userMypageService.findByUserId(userId);

        // 페이먼트 정보 꺼내기
        Payment payment = paymentService.findPayment(user.getUserNo());
        System.out.println("payment = " + payment);

        // 결제승인 Req 채우기
        ApproveReqDto approveReqDto = new ApproveReqDto();
        approveReqDto.setTid(payment.getTId());
        approveReqDto.setCid("TC0ONETIME");
        approveReqDto.setPartner_order_id(payment.getOrders().getReservationNum());
        approveReqDto.setPartner_user_id(userId);
        approveReqDto.setPg_token(pgToken);
        System.out.println("approveReqDto = " + approveReqDto);

        // 결제승인 api
        ApproveResDto approveResDto = paymentService.paymentApprove(approveReqDto);

        // 승인완료되면 payment에 값 넣기
        payment.setType(approveResDto.getPayment_method_type());
        payment.setMethod(approveResDto.getCard_info().getCard_type());
        payment.setIssuerCorp(approveResDto.getCard_info().getKakaopay_issuer_corp());
        payment.setApprovedAt(approveResDto.getApproved_at());
        Payment paymentInfo = paymentService.paymentInfoSave(payment);

        // 승인완료되면 orders 예약 상태값 결제완료로 바꾸기
        Integer result = paymentService.updateOrder(payment.getPaymentNo());

        //결제 완료된 주문 정보 가져오기
        Orders orderInfo = paymentService.findOrder(payment.getPaymentNo());

        model.addAttribute("paymentInfo",paymentInfo);
        model.addAttribute("orderInfo",orderInfo);
        System.out.println("approveResDto = " + approveResDto);

        return "/user/member/paymentConfirm";
    }

    //결제 실패
    @GetMapping("/payment/fail")
    public String paymentFail() {
        System.out.println("실패");
        return "/user/member/paymentConfirm";
    }

    //결제 취소 api
    @PostMapping("/paymentCancel/{orderNo}")
    public String paymentCancel(@PathVariable(value = "orderNo") Integer orderNo , HttpServletResponse response) throws IOException {
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
            return "user/member/reservationList";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('취소 실패 하였습니다.');</script>");
            writer.flush();
            return "user/member/reservationList"; // 원래 리다이렉트를 하면 model.~ 안해도 되지만 alert창과 redirect 같이 사용이 안됨!
        }
    }

}
