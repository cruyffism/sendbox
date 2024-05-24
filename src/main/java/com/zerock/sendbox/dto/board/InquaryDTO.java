package com.zerock.sendbox.dto.board;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquaryDTO {

    private Integer inquaryNo;

    private Integer userNo;

    private String userId;

    private String title;

    private String content;

    private String memberName;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private int replyCount;
}
