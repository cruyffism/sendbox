package com.zerock.sendbox.dto.payment;

import lombok.Data;

@Data
public class PaymentReqDto {

    private String cid; //	가맹점 코드, 10자
    private String partner_order_id; //가맹점 주문번호, 최대 100자
    private String partner_user_id; //가맹점 회원 id, 최대 100자
    private String item_name; //상품명, 최대 100자
    private Integer quantity; //상품 수량
    private Integer total_amount; //상품 총액
    private Integer tax_free_amount; //상품 비과세 금액
    private String approval_url; //결제 성공 시 redirect url, 최대 255자
    private String cancel_url; //결제 취소 시 redirect url, 최대 255자
    private String fail_url; //결제 실패 시 redirect url, 최대 255자

}
