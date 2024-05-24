package com.zerock.sendbox.dto.user;

import jakarta.persistence.Column;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDTO {
    private Integer userNo;

    private String userId;

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
