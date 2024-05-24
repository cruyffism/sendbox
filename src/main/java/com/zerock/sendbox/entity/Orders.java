package com.zerock.sendbox.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate //값이 있는 것만 업데이트
@ToString(exclude = "room")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private UserMember userMember;

    @OneToOne(fetch =  FetchType.LAZY) //fk
    @JoinColumn(name = "paymentNo")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomNo")
    private Room room;

    @Column
    private String reservationNum;

    @Column
    private String reservationStatus;

    @Column
    private Integer totalPrice;

    @Column
    private Integer totalAmount;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;


}
