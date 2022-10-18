package com.example.finalproject1.article.controller;

import com.example.finalproject1.article.entity.Post;
import com.example.finalproject1.article.service.PostService;
import com.example.finalproject1.member.entity.Member;
import com.example.finalproject1.member.service.MemberService;
import com.example.finalproject1.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

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

    @GetMapping("/write")
    public String showPostWrite(){
        return "/post/write";
    }

    @PostMapping("/write")
    public String postWrite(String subject, String content, Principal principal){

        Member member = memberService.findByUsername(principal.getName());

        principal.getName();
        postService.save(subject, content, member);

        return "redirect:/post/list?msg=" + Util.url.encode("글을 작성하였습니다.");
    }

    @GetMapping("/{id}/delete")
    public String postDelete(@PathVariable Long id){

        postService.deleteById(id);

        return "redirect:/post/list?msg=" + Util.url.encode(id + "글이 삭제되었습니다.");
    }

    @GetMapping("/{id}/modify")
    public String showPostModify(@PathVariable Long id, Model model){

        Optional<Post> post = postService.findById(id);

        if(!post.isPresent()){
            return "redirect:/post/list?msg=" + Util.url.encode("해당 글이 존재하지 않습니다.");
        }

        model.addAttribute("post", post.get());

        return "/post/modify";
    }

    @PostMapping("/{id}/modify")
    public String postModify(@PathVariable Long id, String subject, String content){

        Optional<Post> post = postService.findById(id);

        if(!post.isPresent()){
            return "redirect:/post/list?msg=" + Util.url.encode("해당 글이 존재하지 않습니다.");
        }

        postService.save(subject, content, post.get());

        return "redirect:/post/" + id;
    }
}
