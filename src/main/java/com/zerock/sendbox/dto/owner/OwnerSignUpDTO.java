package com.zerock.sendbox.dto.owner;

import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerSignUpDTO {

    private Integer ownerNo;

    private String ownerId;

    private String password;

    private String name;

    @Column(length = 1, columnDefinition = "char(1)")
    private String gender;

    private String mail;

    private String phone;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    @ColumnDefault("'N'")
    @Column(length = 1,  columnDefinition = "char(1)", nullable = false)
    private String deleteYn;
}
