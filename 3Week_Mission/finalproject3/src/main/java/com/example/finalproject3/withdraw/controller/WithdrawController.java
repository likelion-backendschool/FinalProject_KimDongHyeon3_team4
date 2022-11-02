package com.example.finalproject3.withdraw.controller;


import com.example.finalproject3.security.dto.SecurityMember;
import com.example.finalproject3.withdraw.entity.Withdraw;
import com.example.finalproject3.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WithdrawController {

    private final WithdrawService withdrawService;


    @GetMapping("/adm/withdraw/applyList")
    public String showAdmWithdrawApplyList(Model model){

        List<Withdraw> withdraws = withdrawService.findAll();

        model.addAttribute("withdraws", withdraws);

        return "/adm/withdraw/list";
    }
}
