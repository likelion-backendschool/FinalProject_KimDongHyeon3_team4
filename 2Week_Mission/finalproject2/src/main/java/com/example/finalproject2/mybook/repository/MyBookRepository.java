package com.example.finalproject2.mybook.repository;

import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.mybook.entity.MyBook;
import com.example.finalproject2.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    List<MyBook> findByMember(Member member);

    MyBook findByMemberAndProduct(Member member, Product product);
}
