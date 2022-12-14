package com.example.finalproject4.restapi.mybook.dto;

import com.example.finalproject4.mybook.entity.MyBook;
import com.example.finalproject4.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiMyBook {

    Long id;
    LocalDateTime createDate;
    LocalDateTime modifyDate;
    Long ownerId;
    ApiProduct product;

    public static List<ApiMyBook> getApiMyBookByMyBook(List<MyBook> myBooks) {

        List<ApiMyBook> apiMyBooks = new ArrayList<>();

        for(MyBook myBook : myBooks){
            apiMyBooks.add(ApiMyBook.builder()
                            .id(myBook.getId())
                            .createDate(myBook.getCreateDate())
                            .modifyDate(myBook.getModifyDate())
                            .ownerId(myBook.getMember().getId())
                            .product(ApiProduct.builder()
                                    .id(myBook.getProduct().getId())
                                    .createDate(myBook.getProduct().getCreateDate())
                                    .modifyDate(myBook.getProduct().getModifyDate())
                                    .authorId(myBook.getProduct().getAuthor().getId())
                                    .authorName(myBook.getProduct().getAuthor().getNickname())
                                    .subject(myBook.getProduct().getSubject())
                                    .build())
                    .build());
        }

        return apiMyBooks;
    }

    public static ApiMyBook getApiMyBookByMyBook(MyBook myBook, List<Post> posts) {

        List<ApiPost> apiPosts = new ArrayList<>();

        for(Post p : posts){
            apiPosts.add(ApiPost.builder()
                            .id(p.getId())
                            .subject(p.getSubject())
                            .content(p.getContent())
                            .contentHtml(p.getContentHtml())
                            .build());
        }


        ApiMyBook apiMyBook = ApiMyBook.builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .modifyDate(myBook.getModifyDate())
                .ownerId(myBook.getMember().getId())
                .product(ApiProduct.builder()
                        .id(myBook.getProduct().getId())
                        .createDate(myBook.getProduct().getCreateDate())
                        .modifyDate(myBook.getProduct().getModifyDate())
                        .authorId(myBook.getProduct().getAuthor().getId())
                        .authorName(myBook.getProduct().getAuthor().getNickname())
                        .subject(myBook.getProduct().getSubject())
                        .bookChapters(apiPosts)
                        .build())
                .build();

        return apiMyBook;
    }
}
