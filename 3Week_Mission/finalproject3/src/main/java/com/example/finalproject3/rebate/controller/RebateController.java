package com.example.finalproject3.rebate.controller;

import com.example.finalproject3.base.dto.RsData;
import com.example.finalproject3.rebate.entity.RebateOrderItem;
import com.example.finalproject3.rebate.service.RebateService;
import com.example.finalproject3.revenue.entity.Revenue;
import com.example.finalproject3.revenue.service.RevenueService;
import com.example.finalproject3.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
@Slf4j
public class RebateController {

    private final RebateService rebateService;
    private final RevenueService revenueService;

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showCreate() {
        return "adm/rebate/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String create(String yearMonth) {

        RsData data = rebateService.createData(yearMonth);

        String redirect = data.addMsgToUrl("redirect:/adm/rebate/list?yearMonth=" + yearMonth);

        return redirect;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showList(String yearMonth, Model model) {
        if (yearMonth == null) {
            yearMonth = "2022-11";
        }

        List<RebateOrderItem> items = rebateService.findByPayDateIn(yearMonth);

        model.addAttribute("items", items);

        return "adm/rebate/list";
    }

    @PostMapping("/rebateOne/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateOne(@PathVariable long orderItemId, HttpServletRequest req) {
        RsData rebateRsData = rebateService.rebate(orderItemId);

        String referer = req.getHeader("Referer");
        log.debug("referer : " + referer);
        String yearMonth = Util.url.getQueryParamValue(referer, "yearMonth", "");

        String redirect = "redirect:/adm/rebate/list?yearMonth=" + yearMonth;

        redirect = rebateRsData.addMsgToUrl(redirect);

        return redirect;
    }

    @PostMapping("/rebateSelect")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebate(String ids, HttpServletRequest req) {

        String[] idsArr = ids.split(",");

        Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .forEach(id -> {
                    rebateService.rebate(id);
                });

        String referer = req.getHeader("Referer");
        String yearMonth = Util.url.getQueryParamValue(referer, "yearMonth", "");

        String redirect = "redirect:/adm/rebate/list?yearMonth=" + yearMonth;
        redirect += "&msg=" + Util.url.encode("%d?????? ??????????????? ???????????????????????????.".formatted(idsArr.length));

        return redirect;
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRevenue(Model model) {

        List<Revenue> revenues = revenueService.findAll();

        model.addAttribute("revenues",revenues);

        return "adm/rebate/revenue";
    }



}