package com.example.finalproject2.hashtag.entity;

import com.example.finalproject2.keyword.entity.Keyword;
import com.example.finalproject2.post.entity.Post;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class HashTag{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Keyword keyword;

}
