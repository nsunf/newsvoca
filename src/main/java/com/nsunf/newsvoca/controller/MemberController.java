package com.nsunf.newsvoca.controller;

import com.nsunf.newsvoca.config.CustomUserDetails;
import com.nsunf.newsvoca.dto.*;
import com.nsunf.newsvoca.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping({ "", "/" })
    public ResponseEntity<EditMemberFormDto> getEditMemberFormDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EditMemberFormDto editMemberFormDto = memberService.getEditMemberFormDto(authentication.getName());

        log.info(editMemberFormDto.toString());
        return new ResponseEntity<>(editMemberFormDto, HttpStatus.OK);
    }

    @PutMapping({ "", "/" })
    public ResponseEntity<EditMemberFormDto> editMember(EditMemberFormDto requestFormDto) {
        // 프로필 이미지 변경 요청을 분리하여야
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        EditMemberFormDto responseFormDto = memberService.editMember(currentUsername, requestFormDto);

        return new ResponseEntity<>(responseFormDto, HttpStatus.OK);
    }

    @PutMapping("/img")
    public ResponseEntity<String> editMemberImg(List<MultipartFile> memberImgFileList) {
        
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody MemberFormDto memberFormDto) {
        SignupResponseDto signupResponseDto = this.memberService.signup(memberFormDto);

        return new ResponseEntity<>(signupResponseDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginFormDto loginFormDto) {
        TokenDto token = this.memberService.login(loginFormDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);

        return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);
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
