package com.example.finalproject4.post.repository;

import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(Member author);

}
