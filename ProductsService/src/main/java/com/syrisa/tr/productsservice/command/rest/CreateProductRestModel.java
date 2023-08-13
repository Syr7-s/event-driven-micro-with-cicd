package com.syrisa.tr.productsservice.command.rest;

import lombok.*;

import java.math.BigDecimal;

@Data
public class CreateProductRestModel {
    private String title;
    private Integer quantity;
    private BigDecimal price;
}
