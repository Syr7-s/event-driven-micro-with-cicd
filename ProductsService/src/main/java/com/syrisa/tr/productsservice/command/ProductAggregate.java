package com.syrisa.tr.productsservice.command;

import com.syrisa.tr.core.commands.CancelProductReservationCommand;
import com.syrisa.tr.core.commands.ReserveProductCommand;
import com.syrisa.tr.core.events.ProductReservationCancelledEvent;
import com.syrisa.tr.core.events.ProductReservedEvent;
import com.syrisa.tr.productsservice.core.events.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate(snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
public class ProductAggregate {

    @AggregateIdentifier
    private  String  productId;
    private  String title;
    private  Integer quantity;
    private  BigDecimal price;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductAggregate.class);



    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        // Validate Create Product Command
        if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Price cannot be less than or equal to zero");
        }

        if (createProductCommand.getTitle()==null || createProductCommand.getTitle().isBlank()){
            throw new IllegalArgumentException("Title cannot be empty");
        }

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand,productCreatedEvent);
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand){
        // Validate Reserve Product Command
        LOGGER.info("ReserveProductCommand is called for productId: " + reserveProductCommand.getProductId() + " and orderId: " + reserveProductCommand.getOrderId());
        if (reserveProductCommand.getQuantity()<=0){
            throw new IllegalArgumentException("Quantity cannot be less than or equal to zero");
        }

        if (this.quantity<reserveProductCommand.getQuantity()){
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        // Publish Product Reserved Event
        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();

        AggregateLifecycle.apply(productReservedEvent);
        LOGGER.info("ProductReservedEvent is published for productId: " + reserveProductCommand.getProductId() + " and orderId: " + reserveProductCommand.getOrderId());

    }


    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand){
        // Publish Product Reservation Cancelled Event
        ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent.builder()
                .orderId(cancelProductReservationCommand.getOrderId())
                .productId(cancelProductReservationCommand.getProductId())
                .quantity(cancelProductReservationCommand.getQuantity())
                .userId(cancelProductReservationCommand.getUserId())
                .reason(cancelProductReservationCommand.getReason())
                .build();

        AggregateLifecycle.apply(productReservationCancelledEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent){
        // Save Product to the database
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
        this.title = productCreatedEvent.getTitle();
    }

    @EventSourcingHandler // This annotation is used to define the event that will be handled by the aggregate.
    public void on(ProductReservedEvent productReservedEvent){
        // Update Product in the database
        this.quantity -= productReservedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent){
        // Update Product in the database
        this.quantity += productReservationCancelledEvent.getQuantity();
    }

}
