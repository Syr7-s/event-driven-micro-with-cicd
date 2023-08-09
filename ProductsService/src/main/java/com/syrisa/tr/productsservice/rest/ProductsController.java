package com.syrisa.tr.productsservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductsController {

    private final Environment environment;

    @PostMapping
    public String createProduct(){
        return "HTTP Post Handled";
    }

    @GetMapping
    public String getProduct(){
        return "HTTP Get Handled "+ environment.getProperty("local.server.port");
    }

    @PutMapping
    public String updateProduct(){
        return "HTTP Put Handled";
    }

    @DeleteMapping
    public String deleteProduct(){
        return "HTTP Delete Handled";
    }
}
