package com.syrisa.tr.ordersservice.command;

import com.syrisa.tr.ordersservice.command.commands.ApproveOrderCommand;
import com.syrisa.tr.ordersservice.command.commands.CreateOrderCommand;
import com.syrisa.tr.ordersservice.command.commands.RejectOrderCommand;
import com.syrisa.tr.ordersservice.core.events.OrderApprovedEvent;
import com.syrisa.tr.ordersservice.core.events.OrderCreatedEvent;
import com.syrisa.tr.ordersservice.core.events.OrderRejectedEvent;
import com.syrisa.tr.ordersservice.core.model.OrderStatus;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate // Aggregate annotation is used to define the class as an aggregate.
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;


    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        // Validate Create Order Command
        if (createOrderCommand.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity cannot be less than or equal to zero");
        }

        if (createOrderCommand.getProductId() == null || createOrderCommand.getProductId().isBlank()) {
            throw new IllegalArgumentException("Product Id cannot be empty");
        }

        if (createOrderCommand.getUserId() == null || createOrderCommand.getUserId().isBlank()) {
            throw new IllegalArgumentException("User Id cannot be empty");
        }

        if (createOrderCommand.getAddressId() == null || createOrderCommand.getAddressId().isBlank()) {
            throw new IllegalArgumentException("Address Id cannot be empty");
        }

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        // Save Order to the database
        this.orderId = orderCreatedEvent.getOrderId();
        this.productId = orderCreatedEvent.getProductId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.userId = orderCreatedEvent.getUserId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(ApproveOrderCommand approvedOrderCommand) {
    	// Create and publish the OrderApprovedEvent

    	OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approvedOrderCommand.getOrderId());

    	AggregateLifecycle.apply(orderApprovedEvent);
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
    	this.orderStatus = orderApprovedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand) {
    	OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent(rejectOrderCommand.getOrderId(),
    			rejectOrderCommand.getReason());
    	AggregateLifecycle.apply(orderRejectedEvent);
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
    	this.orderStatus = orderRejectedEvent.getOrderStatus();
    }
}
