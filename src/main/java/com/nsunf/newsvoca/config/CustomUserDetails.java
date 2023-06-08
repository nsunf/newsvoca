package com.nsunf.newsvoca.config;

import com.nsunf.newsvoca.constant.MemberStatus;
import com.nsunf.newsvoca.constant.Role;
import com.nsunf.newsvoca.entity.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class CustomUserDetails implements UserDetails {
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private Role role;

    private MemberStatus status;

    public CustomUserDetails(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.password = member.getPassword();
        this.role = member.getRole();
        this.status = member.getStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> result = new ArrayList<>();
        result.add(this.role);
        return result;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status != MemberStatus.LEFT;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != MemberStatus.BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == MemberStatus.DEFAULT;
    }
}
