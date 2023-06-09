package com.nsunf.newsvoca.config;

import com.nsunf.newsvoca.constant.MemberStatus;
import com.nsunf.newsvoca.constant.Role;
import com.nsunf.newsvoca.entity.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private Role role;
    private MemberStatus status;

    private String memberImgUrl;

    public CustomUserDetails(Member member, String memberImgUrl) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.password = member.getPassword();
        this.role = member.getRole();
        this.status = member.getStatus();
        this.memberImgUrl = memberImgUrl;
    }

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.email = username;
        this.password = password;
        authorities.forEach(a -> {
            this.role = Role.valueOf(a.getAuthority());
        });
    }

    public List<String> getRoles() {
        return this.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
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
