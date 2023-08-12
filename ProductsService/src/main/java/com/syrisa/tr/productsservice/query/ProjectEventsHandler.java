package com.syrisa.tr.productsservice.query;

import com.syrisa.tr.productsservice.core.data.ProductEntity;
import com.syrisa.tr.productsservice.core.data.ProductsRepository;
import com.syrisa.tr.productsservice.core.events.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectEventsHandler {

    private final ProductsRepository productsRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);
        productsRepository.save(productEntity);
    }
}
