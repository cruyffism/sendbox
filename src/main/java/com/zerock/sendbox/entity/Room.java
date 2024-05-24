package com.zerock.sendbox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "order")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomNo;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Orders> orders = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeNo")
    private Store store;

    @Column
    private String size;

    @Column
    private Integer price;

    @Column
    private Integer remain;


}
