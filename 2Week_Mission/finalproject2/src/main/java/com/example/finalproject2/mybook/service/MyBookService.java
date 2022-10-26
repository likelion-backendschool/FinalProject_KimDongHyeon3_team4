package com.example.finalproject2.mybook.service;

import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.mybook.entity.MyBook;
import com.example.finalproject2.mybook.repository.MyBookRepository;
import com.example.finalproject2.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyBookService {

    private final MyBookRepository myBookRepository;

    //저장
    public void save(Member member, Product product){

        MyBook myBook = MyBook.builder()
                .member(member)
                .product(product)
                .build();

        myBookRepository.save(myBook);
    }

    public List<MyBook> findByMember(Member member) {

        return myBookRepository.findByMember(member);
    }

    //삭제

}
