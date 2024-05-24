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
@ToString
@Table
@DynamicUpdate
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(nullable = false)
    private  String name;

    @OneToMany(mappedBy = "memberRole", fetch = FetchType.LAZY)
    private List<UserMember> userMembers = new ArrayList<>();

    @OneToMany(mappedBy = "memberRole", fetch = FetchType.LAZY)
    private List<OwnerMember> ownerMembers = new ArrayList<>();

    @OneToMany(mappedBy = "memberRole", fetch = FetchType.LAZY)
    private List<AdminMember> adminMembers = new ArrayList<>();
}
