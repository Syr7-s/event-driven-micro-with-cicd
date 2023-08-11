package com.syrisa.tr.productsservice.core.events;

import lombok.Data;

import java.math.BigDecimal;
@Data

public class ProductCreatedEvent {
    // <Noun><PerformedAction>Event - ProductCreatedEvent
    private  String  productId;
    private  String title;
    private  Integer quantity;
    private  BigDecimal price;
}
