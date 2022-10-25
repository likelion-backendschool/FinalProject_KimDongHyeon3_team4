package com.example.finalproject2.product.service;


import com.example.finalproject2.keyword.entity.Keyword;
import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.product.entity.Product;
import com.example.finalproject2.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void save(Member member, Keyword keyword, String subject, int price) {
        productRepository.save(Product.builder()
                .author(member)
                .keyword(keyword)
                .subject(subject)
                .price(price)
                .fee((int)(price*0.1))
                .build());
    }

    public void save(Product product, String subject, int price) {
        product.setSubject(subject);
        product.setPrice(price);
        productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
