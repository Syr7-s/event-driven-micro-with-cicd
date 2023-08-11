package com.syrisa.tr.productsservice.rest;

import lombok.*;

import java.math.BigDecimal;

@Data
public class CreateProductRestModel {
    private String title;
    private Integer quantity;
    private BigDecimal price;
}
