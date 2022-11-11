package com.example.finalproject4.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
