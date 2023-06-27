package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.config.TokenProvider;
import com.nsunf.newsvoca.dto.*;
import com.nsunf.newsvoca.entity.Member;
import com.nsunf.newsvoca.entity.MemberImg;
import com.nsunf.newsvoca.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberImgService memberImgService;
    private final ModelMapper modelMapper;


    public SignupResponseDto signup(MemberFormDto memberFormDto) {
        if (memberRepository.existsByEmail(memberFormDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 회원입니다.");
        }
        Member member = memberRepository.save(memberFormDto.toEntity(passwordEncoder));
        return modelMapper.map(member, SignupResponseDto.class);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public EditMemberFormDto getEditMemberFormDto(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다."));
        MemberImg memberImg = memberImgService.getMemberImgByMember(member);

        return new EditMemberFormDto(member, memberImg);
    }

    public EditMemberFormDto editMember(String currentUsername, EditMemberFormDto editMemberFormDto) {
        Member member = memberRepository.findById(editMemberFormDto.getId()).orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다."));
        if (member.getEmail().equals(currentUsername)) {
            throw new RuntimeException("인증된 회원과 요청된 회원 정보가 다릅니다.");
        }

        member.update(
                editMemberFormDto.getName(),
                editMemberFormDto.getNickname(),
                editMemberFormDto.getEmail(),
                editMemberFormDto.getPhone1(),
                editMemberFormDto.getPhone2(),
                editMemberFormDto.getPhone3(),
                editMemberFormDto.getAddress1(),
                editMemberFormDto.getAddress2()
        );

        return this.getEditMemberFormDto(member.getEmail());
    }
}
