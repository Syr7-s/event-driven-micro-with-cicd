package com.syrisa.tr.productsservice.query.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-query")
public class ProductQueryController {
    @GetMapping
    public List<ProductRestModel> getProducts(){
        return null;
    }
}
