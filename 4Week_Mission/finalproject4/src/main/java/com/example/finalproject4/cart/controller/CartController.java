package com.example.finalproject4.cart.controller;

import com.example.finalproject4.cart.entity.CartItem;
import com.example.finalproject4.cart.service.CartService;
import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.product.entity.Product;
import com.example.finalproject4.product.service.ProductService;
import com.example.finalproject4.security.dto.SecurityMember;
import com.example.finalproject4.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

        if(cartService.hasItem(securityMember.getMember(), product)){
            return "redirect:/product/" + productId + "?errorMsg=" + Util.url.encode(productId + "번의 상품은 이미 장바구니에 추가되어 있습니다.");
        }

        cartService.save(securityMember.getMember(), product);

        return "redirect:/product/" + productId + "?msg=" + Util.url.encode(productId + "번의 상품이 장바구니에 추가되었습니다.");
    }

    @GetMapping("/remove/{productId}")
    public String removeItems(@AuthenticationPrincipal SecurityMember securityMember, @PathVariable Long productId) {
        Member buyer = securityMember.getMember();

        Product product = productService.findById(productId);

        cartService.delete(buyer, product);

        return "redirect:/cart/list?msg=" + Util.url.encode("%d번의 품목을 삭제하였습니다.".formatted(productId));
    }
}
