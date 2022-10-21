package com.example.finalproject1.member.controller;

import com.example.finalproject1.member.dto.JoinForm;
import com.example.finalproject1.member.entity.Member;
import com.example.finalproject1.member.service.MemberService;
import com.example.finalproject1.security.dto.SecurityMember;
import com.example.finalproject1.security.service.SecurityMemberService;
import com.example.finalproject1.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/findUsername")
    public String showFindUsername(){
        return "/member/findUsername";
    }

    @PostMapping("/findUsername")
    public String findUsername(String email){

        Optional<Member> member = Optional.ofNullable(memberService.findByEmail(email));

        if(!member.isPresent()){
            return "redirect:/member/findUsername?msg=" + Util.url.encode("등록된 아이디가 없습니다.");
        }

        return "redirect:/member/findUsername?msg=" + member.get().getUsername();
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(HttpSession session, @RequestBody HashMap<String, String> email){

        String authKey = UUID.randomUUID().toString().replaceAll("-","").substring(0,6);

        memberService.verifyEmail(email.get("email"), authKey);

        session.setAttribute("authKey", authKey);
        session.setMaxInactiveInterval(5*60);

        return new ResponseEntity<>("Success",HttpStatus.OK);
    }

    @PostMapping("/verifyEmailCode")
    public ResponseEntity<String> verifyEmailCode(HttpSession session, @RequestBody HashMap<String, String> code){

        String authKey = (String)session.getAttribute("authKey");

        System.out.println("authKey = " + authKey);

        if(authKey == null){
            return new ResponseEntity<>("Success",HttpStatus.REQUEST_TIMEOUT);
        }

        if(!authKey.equals(code.get("code"))){
            return new ResponseEntity<>("Success",HttpStatus.NOT_FOUND);
        }

        session.setMaxInactiveInterval(0);

        return new ResponseEntity<>("Success",HttpStatus.OK);
    }

    protected Authentication createNewAuthentication(Authentication currentAuth, String username) {

        SecurityMember newPrincipal = (SecurityMember) securityMemberService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), newPrincipal.getAuthorities());
        newAuth.setDetails(currentAuth.getDetails());
        return newAuth;
    }

}
