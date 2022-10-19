package com.example.finalproject1.hashtag.entity;

import com.example.finalproject1.keyword.entity.Keyword;
import com.example.finalproject1.post.entity.Post;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
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
