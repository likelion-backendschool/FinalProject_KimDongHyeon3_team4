package com.example.finalproject2.cart.controller;

import com.example.finalproject2.cart.entity.CartItem;
import com.example.finalproject2.cart.service.CartService;
import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.member.service.MemberService;
import com.example.finalproject2.security.dto.SecurityMember;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String showCartList(@AuthenticationPrincipal SecurityMember securityMember, Model model){

        Member buyer = securityMember.getMember();

        List<CartItem> cartItems = cartService.findByBuyer(buyer);

        model.addAttribute("cartItems", cartItems);

        return "/cart/list";
    }
}
