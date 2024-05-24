package com.zerock.sendbox.dto.admin;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String adminId;

    private String password;

    private String Mail;
}
