package com.nsunf.newsvoca.controller;

import com.nsunf.newsvoca.config.CustomUserDetails;
import com.nsunf.newsvoca.dto.LoginFormDto;
import com.nsunf.newsvoca.dto.MemberFormDto;
import com.nsunf.newsvoca.dto.TokenDto;
import com.nsunf.newsvoca.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody MemberFormDto memberFormDto) {
        Long id = this.memberService.signup(memberFormDto);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginFormDto loginFormDto) {
        TokenDto token = this.memberService.login(loginFormDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);

        return new ResponseEntity<>(token.getAccessToken(), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails user = null;

        try {
            user = (CustomUserDetails) userDetails;
        } catch (Exception e) {
            log.error(e.getMessage());
        }


        if (user != null)
            return user.toString();
        else
            return "No UsersDetail";
    }
}
