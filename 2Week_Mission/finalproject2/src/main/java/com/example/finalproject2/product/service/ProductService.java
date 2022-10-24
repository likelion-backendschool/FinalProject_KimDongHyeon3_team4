package com.example.finalproject2.product.service;


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
}
