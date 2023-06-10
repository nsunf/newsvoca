package com.nsunf.newsvoca.controller;

import com.nsunf.newsvoca.config.CustomUserDetails;
import com.nsunf.newsvoca.config.TokenProvider;
import com.nsunf.newsvoca.dto.LoginFormDto;
import com.nsunf.newsvoca.dto.LoginFormDto;
import com.nsunf.newsvoca.dto.MemberFormDto;
import com.nsunf.newsvoca.entity.Member;
import com.nsunf.newsvoca.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class MemberController {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final PasswordEncoder pwEncoder;
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody MemberFormDto memberFormDto) {
        Long id = this.memberService.createMember(memberFormDto, this.pwEncoder);

        if (id != null)
            return new ResponseEntity<>(id, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginFormDto loginFormDto) {
        try {
            Member member = memberService.getMember(loginFormDto.getEmail(), loginFormDto.getPassword(), pwEncoder);
            CustomUserDetails userDetails = new CustomUserDetails(member);

            String token = tokenProvider.createToken(userDetails.getUsername(), userDetails.getRoles());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("login failed", HttpStatus.BAD_REQUEST);
        }
    }
}
