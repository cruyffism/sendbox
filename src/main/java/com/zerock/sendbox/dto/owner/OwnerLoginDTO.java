package com.zerock.sendbox.dto.owner;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerLoginDTO {
    private String ownerId;

    private String password;
}
