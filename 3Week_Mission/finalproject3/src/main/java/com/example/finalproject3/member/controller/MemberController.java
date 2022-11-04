package com.example.finalproject3.member.controller;

import com.example.finalproject3.member.dto.JoinForm;
import com.example.finalproject3.member.entity.Member;
import com.example.finalproject3.member.service.MemberService;
import com.example.finalproject3.mybook.entity.MyBook;
import com.example.finalproject3.mybook.service.MyBookService;
import com.example.finalproject3.order.entity.Order;
import com.example.finalproject3.order.service.OrderService;
import com.example.finalproject3.security.dto.SecurityMember;
import com.example.finalproject3.security.service.SecurityMemberService;
import com.example.finalproject3.util.Util;
import com.example.finalproject3.withdraw.entity.Withdraw;
import com.example.finalproject3.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final SecurityMemberService securityMemberService;
    private final WithdrawService withdrawService;
    private final PasswordEncoder passwordEncoder;
    private final MyBookService myBookService;
    private final OrderService orderService;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember securityMember = (SecurityMember) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(authentication,securityMember.getUsername()));
        return "/member/profile";
    }

    @GetMapping("/mybook")
    public String showMyBook(@AuthenticationPrincipal SecurityMember securityMember,
                             Model model){

        Member member = securityMember.getMember();
        List<MyBook> myBooks = myBookService.findByMember(member);

        List<Order> orders = orderService.findByMember(member);

        model.addAttribute("myBooks", myBooks);
        model.addAttribute("orders",orders);

        return "/member/mybook";
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

        Member member = memberService.findByEmail(email);

        if(member == null){
            return "redirect:/member/findUsername?msg=" + Util.url.encode("등록된 아이디가 없습니다.");
        }

        return "redirect:/member/findUsername?msg=" + member.getUsername();
    }

    @GetMapping("/findPassword")
    public String showFindPassword(){
        return "/member/findPassword";
    }

    @PostMapping("/findPassword")
    public String findPassword(String username, String email){

        Member member = memberService.findByUsername(username);

        if(member == null){
            return "redirect:/member/findPassword?msg=" + Util.url.encode("등록된 아이디가 없습니다.");
        }

        if(!member.getEmail().equals(email)){
            return "redirect:/member/findPassword?msg=" + Util.url.encode("등록된 이메일 없습니다.");
        }

        String newPassword = UUID.randomUUID().toString().replaceAll("-","").substring(0,6);
        member.setPassword(passwordEncoder.encode(newPassword));

        memberService.save(member);

        memberService.sendEmail(member.getEmail(), "[e-book market] 임시비밀번호",newPassword);

        return "redirect:/member/login?msg=" + Util.url.encode("임시 비밀번호가 메일로 발송되었습니다.");
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


    @GetMapping("/withdraw/apply")
    public String showWithdrawApply(@AuthenticationPrincipal SecurityMember securityMember,
                                    Model model){

        Member member = securityMember.getMember();

        model.addAttribute("member", member);

        return "/member/withdraw";
    }

    @PostMapping("/withdraw/apply")
    public String withdrawApply(@AuthenticationPrincipal SecurityMember securityMember,
                                String bankName,
                                String bankAccountNo,
                                int price){

        Member member = securityMember.getMember();

        if(member.getRestCash() < price){
            return "redirect:/member/withdraw/apply?msg=" + Util.url.encode("예치금이 부족합니다.");
        }

        withdrawService.save(bankName, bankAccountNo, price, member);

        return "redirect:/member/profile?msg=" + Util.url.encode("%d원 출금 신청이 되었습니다.".formatted(price));
    }

    @GetMapping("/withdraw/list")
    public String showWithdrawList(@AuthenticationPrincipal SecurityMember securityMember,
                                    Model model){

        Member member = securityMember.getMember();

        List<Withdraw> withdraws = withdrawService.findByMember(member);

        model.addAttribute("withdraws", withdraws);

        return "/member/withdrawlist";
    }

    protected Authentication createNewAuthentication(Authentication currentAuth, String username) {

        SecurityMember newPrincipal = (SecurityMember) securityMemberService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), newPrincipal.getAuthorities());
        newAuth.setDetails(currentAuth.getDetails());
        return newAuth;
    }

}
