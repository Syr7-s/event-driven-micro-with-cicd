package com.syrisa.tr.productsservice.query.rest;

import com.syrisa.tr.productsservice.query.FindProductsQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductQueryController {

    private final QueryGateway queryGateway;
    @GetMapping
    public List<ProductRestModel> getProducts(){
        return queryGateway.
                query(new FindProductsQuery(), ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
    }
}
