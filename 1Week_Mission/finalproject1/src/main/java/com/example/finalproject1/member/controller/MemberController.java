package com.example.finalproject1.member.controller;

import com.example.finalproject1.member.dto.JoinForm;
import com.example.finalproject1.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String joinShow(){
        return "/member/join";
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm){
        return "/member/join";
    }
}
