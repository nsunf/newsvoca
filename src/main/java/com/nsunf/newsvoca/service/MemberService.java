package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.config.CustomUserDetails;
import com.nsunf.newsvoca.config.TokenProvider;
import com.nsunf.newsvoca.dto.LoginFormDto;
import com.nsunf.newsvoca.dto.MemberFormDto;
import com.nsunf.newsvoca.dto.TokenDto;
import com.nsunf.newsvoca.entity.Member;
import com.nsunf.newsvoca.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberImgService memberImgService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = this.memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        String memberImgUrl = memberImgService.getMemberImgUrlByEmail(email);

        return new CustomUserDetails(member, memberImgUrl);

//        Member member = this.memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().getAuthority());
//        return new User(member.getEmail(), member.getPassword(), Collections.singleton(grantedAuthority));
    }

    public Long signup(MemberFormDto memberFormDto) {
        if (memberRepository.existsByEmail(memberFormDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 회원입니다.");
        }
        Member member = memberRepository.save(memberFormDto.toEntity(passwordEncoder));
        return member.getId();
    }

    public TokenDto login(LoginFormDto loginFormDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginFormDto.getEmail(), loginFormDto.getPassword());

        Authentication authentication = managerBuilder.getObject().authenticate(authToken);

        return tokenProvider.generateToken(authentication);
    }

    public Long createMember(MemberFormDto memberFormDto, PasswordEncoder pwEncoder) {
        if (memberRepository.existsByEmail(memberFormDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 회원입니다.");
        }
        Member member = memberRepository.save(memberFormDto.toEntity(pwEncoder));
        return member.getId();
    }

    @Transactional(readOnly = true)
    public Member getMember(String email, String password, PasswordEncoder pwEncoder) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("일치하는 회원이 없습니다."));
        if (pwEncoder.matches(password, member.getPassword()))
            return member;
        else
            return null;
    }
}
