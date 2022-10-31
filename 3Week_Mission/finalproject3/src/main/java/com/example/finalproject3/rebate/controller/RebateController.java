package com.example.finalproject3.rebate.controller;

import com.example.finalproject3.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public String create(String yearMonth) {

        rebateService.createData(yearMonth);


return "성공";
        //return "fromDateStr : %s<br>toDateStr : %s".formatted(fromDateStr, toDateStr);
    }

}