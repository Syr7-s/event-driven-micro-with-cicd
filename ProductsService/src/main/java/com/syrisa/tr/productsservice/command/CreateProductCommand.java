package com.syrisa.tr.productsservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
@Builder
@Data
public class CreateProductCommand {
    @TargetAggregateIdentifier
    private final String  productId;
    private final String title;
    private final Integer quantity;
    private final BigDecimal price;
}
