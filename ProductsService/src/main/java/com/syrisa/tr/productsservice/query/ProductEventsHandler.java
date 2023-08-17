package com.syrisa.tr.productsservice.query;

import com.syrisa.tr.core.events.ProductReservationCancelledEvent;
import com.syrisa.tr.core.events.ProductReservedEvent;
import com.syrisa.tr.productsservice.core.data.ProductEntity;
import com.syrisa.tr.productsservice.core.data.ProductsRepository;
import com.syrisa.tr.productsservice.core.events.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@RequiredArgsConstructor
public class ProductEventsHandler {

    private final ProductsRepository productsRepository;

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
       throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        System.out.println("Illegal Argument Exception: " + exception.getMessage());
    }

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);
        try {
            productsRepository.save(productEntity);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

     /*   if (true){
            throw new Exception("Forcing exception in the Event Handler");
        }*/

    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent){
        ProductEntity productEntity = productsRepository.findByProductId(productReservedEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity()-productReservedEvent.getQuantity());
        productsRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
    	ProductEntity productEntity = productsRepository.findByProductId(productReservationCancelledEvent.getProductId());
    	productEntity.setQuantity(productEntity.getQuantity() + productReservationCancelledEvent.getQuantity());
    	productsRepository.save(productEntity);
    }
}
