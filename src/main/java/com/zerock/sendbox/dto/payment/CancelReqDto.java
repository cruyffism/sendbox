package com.zerock.sendbox.dto.payment;

import lombok.Data;

@Data
public class CancelReqDto {
    private String cid; //가맹점 코드, 10자
    private String tid; //결제 고유번호, 20자
    private String cancel_amount; // 취소 금액
    private String cancel_tax_free_amount; //취소 비과세 금액

}
