package com.example.finalproject4.product.service;


import com.example.finalproject4.keyword.entity.Keyword;
import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.product.entity.Product;
import com.example.finalproject4.product.repository.ProductRepository;
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

    public void save(Member member, Keyword keyword, String subject, int salePrice) {
        productRepository.save(Product.builder()
                .author(member)
                .keyword(keyword)
                .subject(subject)
                .price((int)(salePrice*1.6))
                .wholesalePrice((int)(salePrice*0.9))
                .salePrice(salePrice)
                .build());
    }

    public void save(Product product, String subject, int salePrice) {
        product.setSubject(subject);
        product.setSalePrice(salePrice);
        product.setWholesalePrice((int)(salePrice*0.9));
        product.setPrice((int)(salePrice*1.6));
        productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
