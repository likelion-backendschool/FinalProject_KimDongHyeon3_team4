package com.example.finalproject1.article.controller;

import com.example.finalproject1.article.entity.Post;
import com.example.finalproject1.article.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/list")
    public String showPostList(Model model){

        List<Post> posts = postService.findAll();

        model.addAttribute("posts",posts);

        return "/post/list";
    }



}
