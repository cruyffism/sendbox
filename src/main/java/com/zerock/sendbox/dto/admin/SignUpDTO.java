package com.zerock.sendbox.dto.admin;

import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {

    private Integer adminNo;

    private Integer approval;

    private String adminId;

    private String password;

    private String name;

    private String gender;

    private String mail;

    private String phone;

    @Column(length = 3, columnDefinition = "char(3)")
    private String part;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    @ColumnDefault("'N'")
    @Column(length = 1,  columnDefinition = "char(1)", nullable = false)
    private String deleteYn;
}
