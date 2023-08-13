package com.syrisa.tr.productsservice.query.rest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRestModel {
    private String productId;
    private String title;
    private Integer quantity;
    private BigDecimal price;
}
