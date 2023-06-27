package com.nsunf.newsvoca.dto;

import com.nsunf.newsvoca.entity.Member;
import com.nsunf.newsvoca.entity.MemberImg;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class EditMemberFormDto {
    private Long id;

    private String imgUrl;
    private String oriImgName;

    private String name;
    private String nickname;
    private String email;

    private String phone1;
    private String phone2;
    private String phone3;

    private String address1;
    private String address2;

    public EditMemberFormDto(Member member, MemberImg memberImg) {
        this.id = member.getId();
        this.imgUrl = memberImg.getUrl();
        this.oriImgName = memberImg.getOriImgName();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.address1 = member.getAddress1();
        this.address2 = member.getAddress2();

        String[] phoneArr = member.getPhone().split("-");
        this.phone1 = phoneArr[0];
        this.phone2 = phoneArr[1];
        this.phone3 = phoneArr[2];
    }
}
