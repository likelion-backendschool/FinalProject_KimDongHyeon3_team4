package com.example.finalproject1.post.service;

import com.example.finalproject1.hashtag.service.HashTagService;
import com.example.finalproject1.post.entity.Post;
import com.example.finalproject1.post.repository.PostRepository;
import com.example.finalproject1.member.entity.Member;
import com.example.finalproject1.util.MarkDown;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final HashTagService hashTagService;
    private final MarkDown markDown;
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public void save(String subject, String content, Member member) {

        String contentHtml = markDown.markdownWrite(content);

        Post post = Post.builder()
                .author(member)
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .build();

        postRepository.save(post);
    }

    public void save(String subject, String content, Member member, String hashTagStr) {

        String contentHtml = markDown.markdownWrite(content);

        Post post = Post.builder()
                .author(member)
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .build();

        postRepository.save(post);

        hashTagService.applyHashTags(post, hashTagStr);
    }

    public void save(String subject, String content, Post post) {

        String contentHtml = markDown.markdownWrite(content);

        post.setSubject(subject);
        post.setContent(content);
        post.setContentHtml(contentHtml);

        postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
