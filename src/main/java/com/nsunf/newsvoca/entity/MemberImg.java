package com.nsunf.newsvoca.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class MemberImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_img_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @Column
    private String url;
    @Column
    private String filename;
    @Column
    private String oriImgName;
}
