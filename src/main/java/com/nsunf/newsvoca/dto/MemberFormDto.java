package com.nsunf.newsvoca.dto;

import com.nsunf.newsvoca.constant.MemberStatus;
import com.nsunf.newsvoca.constant.Role;
import com.nsunf.newsvoca.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public class MemberFormDto {
    private String name;
    private String nickname;

    private String ssn1;
    private String ssn2;

    private String email;

    private String password;

    private String phone1;
    private String phone2;
    private String phone3;

    private String address1;
    private String address2;

    public Member toEntity(PasswordEncoder pwEncoder) {
        Member member = new Member();
        member.setName(this.name);
        member.setNickname(this.nickname);
        member.setSsn(String.join("-", this.ssn1, this.ssn2));
        member.setEmail(this.email);
        member.setPassword(pwEncoder.encode(this.password));
        member.setPhone(String.join("-", this.phone1, this.phone2, this.phone3));
        member.setAddress1(this.address1);
        member.setAddress2(this.address2);
        member.setRole(Role.ROLE_USER);
        member.setStatus(MemberStatus.DEFAULT);

        return member;
    }
}