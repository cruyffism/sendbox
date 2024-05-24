package com.zerock.sendbox.dto.board;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardDTO {

    private Integer boardNo;

    private Integer adminNo;

    private String title;

    private String content;

    private String thumbnail;

    private String writerName; //작성자의 이름

    private int AdminAnswerCount;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
