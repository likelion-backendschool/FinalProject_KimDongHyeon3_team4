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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/adm")
@RequiredArgsConstructor
@Slf4j
public class AdmMemberController {


    @GetMapping("/home")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showHome(){
        return "/adm/home";
    }

}
