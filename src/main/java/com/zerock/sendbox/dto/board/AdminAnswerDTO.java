package com.zerock.sendbox.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminAnswerDTO {

    private Integer answerNo;

    private Integer boardNo;

    private String replyer;

    private String content;

    private LocalDateTime regDate, modDate;


}
