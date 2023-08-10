package com.syrisa.tr.productsservice.rest;

import lombok.*;

@Data
public class CreateProductRestModel {
    private String title;
    private String quantity;
    private String price;
}
