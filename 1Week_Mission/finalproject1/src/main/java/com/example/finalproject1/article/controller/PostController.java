package com.example.finalproject1.article.controller;

import com.example.finalproject1.article.entity.Post;
import com.example.finalproject1.article.service.PostService;
import com.example.finalproject1.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public String showPostDetail(@PathVariable Long id, Model model){

        Optional<Post> post = postService.findById(id);

        if(!post.isPresent()){
            return "redirect:/post/list?msg=" + Util.url.encode("해당 글이 존재하지 않습니다.");
        }

        model.addAttribute("post",post.get());

        return "/post/detail";
    }



}
