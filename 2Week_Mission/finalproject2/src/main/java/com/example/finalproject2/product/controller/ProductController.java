package com.example.finalproject2.product.controller;

import com.example.finalproject2.post.entity.Post;
import com.example.finalproject2.product.entity.Product;
import com.example.finalproject2.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public String showProductList(Model model){

        List<Product> products = productService.findAll();

        model.addAttribute("products", products);
        return "/product/list";
    }
}
