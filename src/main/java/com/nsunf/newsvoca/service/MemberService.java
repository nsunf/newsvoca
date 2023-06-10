package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.config.CustomUserDetails;
import com.nsunf.newsvoca.dto.MemberFormDto;
import com.nsunf.newsvoca.entity.Member;
import com.nsunf.newsvoca.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = this.memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(member);
    }

    public Long createMember(MemberFormDto memberFormDto, PasswordEncoder pwEncoder) {
        Member member = memberFormDto.toEntity(pwEncoder);

        Member saved = memberRepository.save(member);
        return saved.getId();
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
