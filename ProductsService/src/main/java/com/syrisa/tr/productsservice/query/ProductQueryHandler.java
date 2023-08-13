package com.syrisa.tr.productsservice.query;

import com.syrisa.tr.productsservice.core.data.ProductsRepository;
import com.syrisa.tr.productsservice.query.rest.ProductRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductQueryHandler {
    private final ProductsRepository productsRepository;

    @QueryHandler
    public List<ProductRestModel> findProducts() {
        return productsRepository.findAll().stream().map(product -> {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(product, productRestModel);
            return productRestModel;
        }).collect(Collectors.toList());
   /*     Second Solution
        return productsRepository.findAll().stream().map(product -> {
            ProductRestModel productRestModel = new ProductRestModel();
            productRestModel.setProductId(product.getProductId());
            productRestModel.setTitle(product.getTitle());
            productRestModel.setPrice(product.getPrice());
            productRestModel.setQuantity(product.getQuantity());
            return productRestModel;
        }).collect(Collectors.toList());
        Third Solution
        List<ProductRestModel> productRestModels = new ArrayList<>();

        List<ProductEntity> products = productsRepository.findAll();

        for (ProductEntity product : products) {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(product, productRestModel);
            productRestModels.add(productRestModel);
        }
        return productRestModels;*/
    }
}
