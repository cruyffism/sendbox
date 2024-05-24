package com.zerock.sendbox.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "rooms")
@Table(name = "store")
@DynamicUpdate
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeNo;


    @OneToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "ownerNo")
    private OwnerMember ownerMember;

    @Column
    private  String storeName;

    @Column
    private  String notice;


    @Column(length = 3, columnDefinition = "char(3)")
    private String region;

    @Column
    private  String address;

    @Column
    private String brn;

    @Column
    private  String phone;

    @Column
    private  String thumbnail;

    @Column
    private  String infoPhoto;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Room> rooms = new ArrayList<>();

}
