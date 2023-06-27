package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.config.CustomUserDetails;
import com.nsunf.newsvoca.entity.Member;
import com.nsunf.newsvoca.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberImgService memberImgService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = this.memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        String memberImgUrl = memberImgService.getMemberImgUrlByEmail(email);

        return new CustomUserDetails(member, memberImgUrl);
    }
}
