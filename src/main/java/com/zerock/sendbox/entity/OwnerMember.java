package com.zerock.sendbox.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "store")
@DynamicUpdate //값이 있는 것만 업데이트
public class OwnerMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer ownerNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private MemberRole memberRole;

    @OneToOne(mappedBy = "ownerMember", fetch = FetchType.LAZY)
    private Store store;

    @Column
    private String ownerId;

    @Column
    private String password;

    @Column
    private String name;

    @Column(length = 1, columnDefinition = "char(1)")
    private String gender;

    @Column
    private String mail;

    @Column
    private String phone;

    @ColumnDefault("'N'")
    @Column(length = 1,  columnDefinition = "char(1)", nullable = false)
    private String approvalYn;

    @ColumnDefault("'N'")
    @Column(length = 1,  columnDefinition = "char(1)", nullable = false)
    private String deleteYn;

}
