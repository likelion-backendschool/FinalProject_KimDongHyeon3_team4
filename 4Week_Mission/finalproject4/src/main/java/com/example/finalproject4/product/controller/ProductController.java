package com.example.finalproject4.product.controller;

import com.example.finalproject4.hashtag.service.HashTagService;
import com.example.finalproject4.keyword.entity.Keyword;
import com.example.finalproject4.keyword.service.KeywordService;
import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.member.service.MemberService;
import com.example.finalproject4.post.entity.Post;
import com.example.finalproject4.post.service.PostService;
import com.example.finalproject4.product.dto.ProductForm;
import com.example.finalproject4.product.entity.Product;
import com.example.finalproject4.product.service.ProductService;
import com.example.finalproject4.security.dto.SecurityMember;
import com.example.finalproject4.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final KeywordService keywordService;
    private final MemberService memberService;
    private final PostService postService;
    private final HashTagService hashTagService;

    @GetMapping("/list")
    public String showProductList(Model model){

        List<Product> products = productService.findAll();

        model.addAttribute("products", products);
        return "/product/list";
    }

    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id, Model model){

        Product product = productService.findById(id);

        if(product == null){
            return "redirect:/product/list?msg=" + Util.url.encode("?????? ????????? ???????????? ????????????.");
        }

        model.addAttribute("product",product);

        return "/product/detail";
    }

    @GetMapping("/create")
    public String showProductCreate(@AuthenticationPrincipal SecurityMember securityMember,
                                    Model model){


        Member member = securityMember.getMember();

        log.info("????????? = {}", member.getNickname());

        if(member.getNickname().equals("")){
            return "redirect:/product/list?errorMsg=" + Util.url.encode("???????????? ???????????? ???????????????.");
        }

        List<Post> posts = postService.findByAuthor(member);

        HashSet<Keyword> keywordHashSet = new HashSet<>();

        for(Post p : posts){
            List<Keyword> findKeywords = hashTagService.findByPost(p);
            for(Keyword k : findKeywords){
                keywordHashSet.add(k);
            }
        }

        List<Keyword> keywords = new ArrayList<>();

        for(Keyword keyword : keywordHashSet){
            keywords.add(keyword);
        }

        model.addAttribute("keywords", keywords);

        return "product/create";
    }

    @PostMapping("/create")
    public String productCreate(ProductForm productForm, Principal principal){

        Member member = memberService.findByUsername(principal.getName());
        Keyword keyword = keywordService.findByContent(productForm.getKeyword());

        productService.save(member, keyword, productForm.getSubject(), productForm.getSalePrice());

        return "redirect:/product/list?msg=" + Util.url.encode("????????? ?????????????????????.");
    }

    @GetMapping("/{id}/modify")
    public String showProductModify(@PathVariable Long id, Model model){

        Product product = productService.findById(id);

        if(product == null){
            return "redirect:/product/list?msg=" + Util.url.encode("?????? ????????? ???????????? ????????????.");
        }

        model.addAttribute("product", product);

        return "product/modify";
    }

    @PostMapping("/{id}/modify")
    public String productModify(@PathVariable Long id, ProductForm productForm){

        Product product = productService.findById(id);
        productService.save(product, productForm.getSubject(), productForm.getSalePrice());

        return "redirect:/product/" + id + "?msg=" + Util.url.encode(id + "????????? ?????????????????????.");
    }

    @GetMapping("/{id}/delete")
    public String productDelete(@PathVariable Long id){

        productService.deleteById(id);

        return "redirect:/product/list?msg=" + Util.url.encode(id + "????????? ?????????????????????.");
    }
}
