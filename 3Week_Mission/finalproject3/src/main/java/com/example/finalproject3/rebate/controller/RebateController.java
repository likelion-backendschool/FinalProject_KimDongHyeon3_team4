package com.example.finalproject3.rebate.controller;

import com.example.finalproject3.rebate.entity.RebateOrderItem;
import com.example.finalproject3.rebate.service.RebateService;
import com.example.finalproject3.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
public class RebateController {

    private final RebateService rebateService;

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showCreate() {
        return "adm/rebate/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String create(String yearMonth) {

        rebateService.createData(yearMonth);

        return "redirect:/adm/rebate/list?yearMonth=" + yearMonth + "&msg=" + Util.url.encode("정산데이터가 성공적으로 생성되었습니다.");
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showList(String yearMonth, Model model) {
        if (yearMonth == null) {
            yearMonth = "2022-10";
        }

        List<RebateOrderItem> items = rebateService.findByPayDateIn(yearMonth);

        model.addAttribute("items", items);

        return "adm/rebate/list";
    }

}