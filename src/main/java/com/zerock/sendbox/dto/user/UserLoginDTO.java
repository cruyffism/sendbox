package com.zerock.sendbox.dto.user;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    private String userId;

    private String password;
}
