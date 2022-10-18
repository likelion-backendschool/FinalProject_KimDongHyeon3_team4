package com.example.finalproject1.article.service;

import com.example.finalproject1.article.entity.Post;
import com.example.finalproject1.article.repository.PostRepository;
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

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
