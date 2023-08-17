package com.syrisa.tr.ordersservice.saga;

import com.syrisa.tr.core.commands.ProcessPaymentCommand;
import com.syrisa.tr.core.commands.ReserveProductCommand;
import com.syrisa.tr.core.events.PaymentProcessedEvent;
import com.syrisa.tr.core.events.ProductReservedEvent;
import com.syrisa.tr.core.model.User;
import com.syrisa.tr.core.query.FetchUserPaymentDetailsQuery;
import com.syrisa.tr.ordersservice.core.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@RequiredArgsConstructor
public class OrderSaga {

    private final transient CommandGateway commandGateway;

    private final transient QueryGateway queryGateway;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSaga.class);


    @StartSaga // This annotation is used to start the saga.
    @SagaEventHandler(associationProperty = "orderId") // This annotation is used to define the event that will be handled by the saga.
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        LOGGER.info("We are creating a saga for the order with id: " + orderCreatedEvent.getOrderId());
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();
        LOGGER.info("In OrderSaga OrderCreatedEvent handled for orderId: " + reserveProductCommand.getOrderId() +
                " and productId: " + reserveProductCommand.getProductId());
        commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends ReserveProductCommand> commandMessage, @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    // Start a compensating transaction
                } else {
                    // Send an ApproveOrderCommand to the CommandGateway
                }
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        // Send a ProcessPaymentCommand to the CommandGateway
        LOGGER.info("ProductReservedEvent is called for productId: " + productReservedEvent.getProductId() +
                " and orderId: " + productReservedEvent.getOrderId());
        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
        User userPaymentDetails = null;
        try {
            // Send a ProcessPaymentCommand to the CommandGateway
            userPaymentDetails  = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception e) {
            // Start a compensating transaction
            LOGGER.error(e.getMessage());
            return;
        }
        if (userPaymentDetails == null) {
            // Start a compensating transaction
            LOGGER.error("UserPaymentDetails is null for userId: " + productReservedEvent.getUserId());
            return;
        }
        LOGGER.info("UserPaymentDetails fetched for userId: " + userPaymentDetails.getUserId());

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .build();
        String result = null;
        try{
           result =  commandGateway.sendAndWait(processPaymentCommand,10, TimeUnit.SECONDS);
        }catch (Exception e){
            // Start a compensating transaction
            LOGGER.error(e.getMessage());
        }

        LOGGER.info("The ProcessPaymentCommand resulted in NULL. Initiating compensating transaction...");

    }

    @SagaEventHandler(associationProperty = "orderId")
     public void handle(PaymentProcessedEvent paymentProcessedEvent){
        LOGGER.info("PaymentProcessedEvent is called for orderId: " + paymentProcessedEvent.getOrderId());
        // Send a ApproveOrderCommand to the CommandGateway
        }
}
