package com.zerock.sendbox.dto.payment;

import lombok.Data;

@Data
public class ApproveReqDto {
    private String cid; //가맹점 코드, 10자
    private String tid; //결제 고유번호, 결제 준비 API 응답에 포함
    private String partner_order_id; //가맹점 주문번호, 결제 준비 API 요청과 일치해야 함
    private String partner_user_id; //가맹점 회원 id, 결제 준비 API 요청과 일치해야 함
    private String pg_token; // 결제승인 요청을 인증하는 토큰


}
