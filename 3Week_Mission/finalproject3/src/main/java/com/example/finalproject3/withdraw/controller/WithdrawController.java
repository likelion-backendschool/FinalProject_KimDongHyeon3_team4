package com.example.finalproject3.withdraw.controller;


import com.example.finalproject3.base.dto.RsData;
import com.example.finalproject3.util.Util;
import com.example.finalproject3.withdraw.entity.Withdraw;
import com.example.finalproject3.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WithdrawController {

    private final WithdrawService withdrawService;


    @GetMapping("/adm/withdraw/applyList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAdmWithdrawApplyList(Model model){

        List<Withdraw> withdraws = withdrawService.findAll();

        model.addAttribute("withdraws", withdraws);

        return "/adm/withdraw/list";
    }

    @PostMapping("/adm/withdraw/{withdrawApplyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String withdrawOne(@PathVariable long withdrawApplyId) {

        RsData withdrawData = withdrawService.withdrawApply(withdrawApplyId);

        String redirect = "redirect:/adm/withdraw/applyList";

        redirect = withdrawData.addMsgToUrl(redirect);

        return redirect;
    }

    @PostMapping("/adm/withdrawSelect")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String withdraw(String ids, HttpServletRequest req) {

        String[] idsArr = ids.split(",");

        Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .forEach(id -> {
                    withdrawService.withdrawApply(id);
                });;

        return "redirect:/adm/withdraw/applyList?msg=" + Util.url.encode("%d건의 출금목록을 출금처리하였습니다.".formatted(idsArr.length));
    }



}
