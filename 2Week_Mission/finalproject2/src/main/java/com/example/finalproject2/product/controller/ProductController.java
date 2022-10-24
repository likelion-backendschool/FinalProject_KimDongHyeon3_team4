package com.example.finalproject2.product.controller;

import com.example.finalproject2.keyword.entity.Keyword;
import com.example.finalproject2.keyword.service.KeywordService;
import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.member.service.MemberService;
import com.example.finalproject2.post.entity.Post;
import com.example.finalproject2.product.dto.ProductForm;
import com.example.finalproject2.product.entity.Product;
import com.example.finalproject2.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final KeywordService keywordService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String showProductList(Model model){

        List<Product> products = productService.findAll();

        model.addAttribute("products", products);
        return "/product/list";
    }

    @GetMapping("/create")
    public String showProductCreate(Model model){

        List<Keyword> keywords = keywordService.findAll();

        model.addAttribute("keywords", keywords);

        return "product/create";
    }

    @PostMapping("/create")
    public String productCreate(ProductForm productForm, Principal principal){

        Member member = memberService.findByUsername(principal.getName());
        Keyword keyword = keywordService.findByContent(productForm.getKeyword());

        log.info("subject = {}", productForm.getSubject());

        productService.save(member, keyword, productForm.getSubject(), productForm.getPrice());

        return "redirect:/product/list";
    }
}
