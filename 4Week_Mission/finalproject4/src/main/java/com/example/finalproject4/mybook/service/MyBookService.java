package com.example.finalproject4.mybook.service;

import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.mybook.entity.MyBook;
import com.example.finalproject4.mybook.repository.MyBookRepository;
import com.example.finalproject4.product.entity.Product;
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

    public void delete(Member member, Product product) {

        MyBook myBooks = myBookRepository.findByMemberAndProduct(member, product);

        myBookRepository.delete(myBooks);

    }


    public MyBook findById(Long id) {
        return myBookRepository.findById(id).orElse(null);
    }
}
