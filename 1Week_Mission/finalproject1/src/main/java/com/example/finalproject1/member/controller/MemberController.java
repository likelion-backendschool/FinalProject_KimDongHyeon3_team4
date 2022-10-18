package com.example.finalproject1.member.controller;

import com.example.finalproject1.member.dto.JoinForm;
import com.example.finalproject1.member.entity.Member;
import com.example.finalproject1.member.service.MemberService;
import com.example.finalproject1.security.dto.SecurityMember;
import com.example.finalproject1.security.service.SecurityMemberService;
import com.example.finalproject1.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final SecurityMemberService securityMemberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public String showJoin(){
        return "/member/join";
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm){
        memberService.save(joinForm.getUsername(), joinForm.getPassword(), joinForm.getEmail(), joinForm.getNickname());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLogin(){
        return "/member/login";
    }

//    @PostMapping("/login")
//    public String login(String username, String password){
//        System.out.println("username = " + username);
//        return "redirect:/";
//    }
//    @GetMapping("/logout")
//    public String logout(){
//        return "redirect:/";
//    }

    @GetMapping("/profile")
    public String showProfile(){
        return "/member/profile";
    }

    @GetMapping("/modify")
    public String showModifyNickname(){
        return "/member/modify";
    }

    @PostMapping("/modify")
    public String modifyNickname(Principal principal, String modifynickname){

        Member member = memberService.findByUsername(principal.getName());

        member.setNickname(modifynickname);

        memberService.save(member);

        //변경된 회원 정보를 Security에 적용 (타임리프에 적용하기 위함)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember securityMember = (SecurityMember) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(authentication,securityMember.getUsername()));

        return "redirect:/member/profile";
    }

    @GetMapping("/modifyPassword")
    public String showModifyPassword(){
        return "/member/modifypassword";
    }

    @PostMapping("/modifyPassword")
    public String modifyPassword(Principal principal, String modifypassword, String password){

        Member member = memberService.findByUsername(principal.getName());

        log.info("passwordEncoder.matches(password, member.getPassword()) = {} ", passwordEncoder.matches(password, member.getPassword()));

        if(!passwordEncoder.matches(password, member.getPassword())){
            return "redirect:/member/modifyPassword?msg=" + Util.url.encode("비밀번호가 일치하지 않습니다.");
        }

        member.setPassword(passwordEncoder.encode(modifypassword));

        memberService.save(member);

        //변경된 회원 정보를 Security에 적용 (타임리프에 적용하기 위함)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember securityMember = (SecurityMember) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(authentication,securityMember.getUsername()));

        return "redirect:/member/profile?msg=" + Util.url.encode("비밀번호가 올바르게 변경되었습니다.");
    }

    protected Authentication createNewAuthentication(Authentication currentAuth, String username) {

        SecurityMember newPrincipal = (SecurityMember) securityMemberService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), newPrincipal.getAuthorities());
        newAuth.setDetails(currentAuth.getDetails());
        return newAuth;
    }

}
