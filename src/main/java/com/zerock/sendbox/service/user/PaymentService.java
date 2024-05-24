package com.zerock.sendbox.service.user;

import com.zerock.sendbox.dto.payment.*;
import com.zerock.sendbox.entity.Orders;
import com.zerock.sendbox.entity.Payment;
import com.zerock.sendbox.feign.PaymentClient;
import com.zerock.sendbox.repository.OrdersRepository;
import com.zerock.sendbox.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    //인터페이스 활용
    @Autowired
    PaymentClient paymentClient;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrdersRepository ordersRepository;

    //결제 요청
    public PaymentResDto paymentRequest(PaymentReqDto paymentReqDto) {
        return paymentClient.paymentRequest(paymentReqDto);
    }

    // tid payment에 저장
    @Transactional
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    // orders에 paymentId 저장
    @Transactional
    public int payment(Integer orderNo, Integer paymentNo) {
        return ordersRepository.payment(orderNo, paymentNo);
    }

    //페이먼트 정보 꺼내기
    public Payment findPayment(Integer userNo) {
        return paymentRepository.findPayment(userNo);
    }


    //결제승인
    public ApproveResDto paymentApprove(ApproveReqDto approveReqDto) {
        return paymentClient.paymentApprove(approveReqDto);
    }

    @Transactional
    // 승인완료되면 orders 예약 상태값 결제완료로 바꾸기
    public Integer updateOrder(Integer paymentNo) {
        return ordersRepository.updateOrder(paymentNo);
    }

    // 승인완료되면 payment에 값 넣기
    public Payment paymentInfoSave(Payment payment) {
        return paymentRepository.save(payment);
    }

    //결제 완료된 주문 정보 가져오기
    public Orders findOrder(Integer paymentNo) {
        return ordersRepository.findOrder(paymentNo);
    }

    @Transactional
    //결제 취소 api
    public Integer paymentCancel(Integer orderNo) {
        return ordersRepository.paymentCancel(orderNo);
    }

    //tid 및 가격 찾기 위한 메서드
    public Orders findPaymentInfo(Integer orderNo) {
        return ordersRepository.findPaymentInfo(orderNo);
    }

    public CancelResDto paymentCancel(CancelReqDto cancelReqDto) {
        return paymentClient.paymentCancel(cancelReqDto);
    }
}
