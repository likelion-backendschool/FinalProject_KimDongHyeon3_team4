package com.example.finalproject2.product.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ProductForm {

    String subject;
    int salePrice;
    String keyword;
}
