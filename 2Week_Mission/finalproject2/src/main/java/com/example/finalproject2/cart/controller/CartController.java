package com.example.finalproject2.cart.controller;

import com.example.finalproject2.cart.entity.CartItem;
import com.example.finalproject2.cart.service.CartService;
import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.member.service.MemberService;
import com.example.finalproject2.product.entity.Product;
import com.example.finalproject2.product.service.ProductService;
import com.example.finalproject2.security.dto.SecurityMember;
import com.example.finalproject2.util.Util;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("/list")
    public String showCartList(@AuthenticationPrincipal SecurityMember securityMember, Model model){

        Member buyer = securityMember.getMember();

        List<CartItem> cartItems = cartService.findByBuyer(buyer);

        model.addAttribute("cartItems", cartItems);

        return "/cart/list";
    }

    @GetMapping("/add/{productId}")
    public String cartAdd(@AuthenticationPrincipal SecurityMember securityMember,@PathVariable Long productId){

        Product product = productService.findById(productId);

        cartService.save(securityMember.getMember(), product);

        return "redirect:/product/" + productId + "?msg=" + Util.url.encode(productId + "번의 상품이 장바구니에 추가되었습니다.");
    }
}
