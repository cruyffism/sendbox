package com.zerock.sendbox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "board")
public class AdminAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer answerNo;

    @Column(nullable = false)
    private String replyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardNo", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String content;




}
