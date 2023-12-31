package com.syrisa.tr.ordersservice.saga;

import com.syrisa.tr.core.commands.CancelProductReservationCommand;
import com.syrisa.tr.core.commands.ProcessPaymentCommand;
import com.syrisa.tr.core.commands.ReserveProductCommand;
import com.syrisa.tr.core.events.PaymentProcessedEvent;
import com.syrisa.tr.core.events.ProductReservationCancelledEvent;
import com.syrisa.tr.core.events.ProductReservedEvent;
import com.syrisa.tr.core.model.User;
import com.syrisa.tr.core.query.FetchUserPaymentDetailsQuery;
import com.syrisa.tr.ordersservice.command.commands.ApproveOrderCommand;
import com.syrisa.tr.ordersservice.command.commands.RejectOrderCommand;
import com.syrisa.tr.ordersservice.core.events.OrderApprovedEvent;
import com.syrisa.tr.ordersservice.core.events.OrderCreatedEvent;
import com.syrisa.tr.ordersservice.core.events.OrderRejectedEvent;
import com.syrisa.tr.ordersservice.core.model.OrderSummary;
import com.syrisa.tr.ordersservice.query.FindOrdersQuery;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient DeadlineManager deadlineManager;

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    private static final String PAYMENT_PROCESSING_DEADLINE = "paymentProcessingDeadline";

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSaga.class);

    private String scheduleId;


    @StartSaga // This annotation is used to start the saga.
    @SagaEventHandler(associationProperty = "orderId")
    // This annotation is used to define the event that will be handled by the saga.
    public void handle(OrderCreatedEvent orderCreatedEvent) {

        LOGGER.info("We are creating a saga for the order with id: " + orderCreatedEvent.getOrderId());
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();
        LOGGER.info("In OrderSaga OrderCreatedEvent handled for orderId: " + reserveProductCommand.getOrderId() + " and productId: " + reserveProductCommand.getProductId());
      /*  commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // Start a compensating transaction
                LOGGER.info("ReserveProductCommand is failed for orderId: " + reserveProductCommand.getOrderId() +
                        " and productId: " + reserveProductCommand.getProductId());
            } else {
                // Send an ApproveOrderCommand to the CommandGateway
                LOGGER.info("ReserveProductCommand is successful for orderId: " + reserveProductCommand.getOrderId() +
                        " and productId: " + reserveProductCommand.getProductId());
            }
        });
        commandGateway.send(reserveProductCommand);
        LOGGER.info("ReserveProductCommand is successful for orderId: " + reserveProductCommand.getOrderId() +
                " and productId: " + reserveProductCommand.getProductId());*/

        commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

            @Override
            public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
                                 CommandResultMessage<? extends Object> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    // Start a compensating transaction
                    RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(orderCreatedEvent.getOrderId(),
                            commandResultMessage.exceptionResult().getMessage());

                    commandGateway.send(rejectOrderCommand);
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
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception e) {
            // Start a compensating transaction
            LOGGER.error(e.getMessage());
            cancelProductReservation(productReservedEvent, e.getMessage());
            return;
        }
        if (userPaymentDetails == null) {
            // Start a compensating transaction
            LOGGER.error("UserPaymentDetails is null for userId: " + productReservedEvent.getUserId());
            cancelProductReservation(productReservedEvent, "UserPaymentDetails is null for userId: " + productReservedEvent.getUserId());
            return;
        }
        LOGGER.info("UserPaymentDetails fetched for userId: " + userPaymentDetails.getUserId());

        scheduleId = deadlineManager.schedule(Duration.of(120, ChronoUnit.SECONDS),
                PAYMENT_PROCESSING_DEADLINE, productReservedEvent);

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .build();
        LOGGER.info("Sending a ProcessPaymentCommand to the CommandGateway for orderId: " + processPaymentCommand.getOrderId() + " and paymentId: " + processPaymentCommand.getPaymentId() + " and paymentDetails: " + processPaymentCommand.getPaymentDetails());
        String result = null;

        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 50, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Start a compensating transaction
            LOGGER.error(e.getMessage());

            cancelProductReservation(productReservedEvent, e.getMessage());
            return;

        }

        if (result == null) {
            // Start a compensating transaction
            LOGGER.error("The ProcessPaymentCommand resulted in NULL. Initiating compensating transaction...");
            cancelProductReservation(productReservedEvent, "The ProcessPaymentCommand resulted in NULL. Initiating compensating transaction...");

        }

    }

    private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {
        LOGGER.info("Sending a CancelProductReservationCommand to compensate the transaction for productId: " +
                productReservedEvent.getProductId() + " and orderId: " + productReservedEvent.getOrderId());
        // Send a CancelProductReservationCommand to the CommandGateway
        cancelDeadline();

        CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .productId(productReservedEvent.getProductId())
                .quantity(productReservedEvent.getQuantity())
                .userId(productReservedEvent.getUserId())
                .reason(reason)
                .build();

        commandGateway.send(cancelProductReservationCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        cancelDeadline();

        LOGGER.info("PaymentProcessedEvent is called for orderId: " + paymentProcessedEvent.getOrderId());
        // Send a ApproveOrderCommand to the CommandGateway
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
        commandGateway.send(approveOrderCommand);
    }

    private void cancelDeadline() {
        if (scheduleId != null) {
            deadlineManager.cancelSchedule(PAYMENT_PROCESSING_DEADLINE, scheduleId);
            scheduleId = null;
        }
        // deadlineManager.cancelAll(PAYMENT_PROCESSING_DEADLINE);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent) {
        LOGGER.info("Order i approved.Order Saga is complete for orderId: " + orderApprovedEvent.getOrderId());
        // Send a ApproveOrderCommand to the CommandGateway
        //  ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(orderApprovedEvent.getOrderId());
        //  commandGateway.send(approveOrderCommand);
        // SagaLifecycle.end();

        queryUpdateEmitter.emit(FindOrdersQuery.class,
                query -> true,
                new OrderSummary(orderApprovedEvent.getOrderId(),
                        orderApprovedEvent.getOrderStatus(),
                        ""));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
        // Create and send a RejectOrderCommand
        LOGGER.info("ProductReservationCancelledEvent is called for orderId: " + productReservationCancelledEvent.getOrderId());
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelledEvent.getOrderId(),
                productReservationCancelledEvent.getReason());
        commandGateway.send(rejectOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent) {
        LOGGER.info("Successfully rejected order with id  " + orderRejectedEvent.getOrderId());
        queryUpdateEmitter.emit(FindOrdersQuery.class,
                query -> true,
                new OrderSummary(orderRejectedEvent.getOrderId(),
                        orderRejectedEvent.getOrderStatus(),
                        orderRejectedEvent.getReason()));
    }

    @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_DEADLINE)
    public void handlePaymentDeadline(ProductReservedEvent productReservedEvent) {
        LOGGER.info("Payment Processing Deadline took place. Sending a compensating command to cancel the product reservation for productId: " +
                productReservedEvent.getProductId() + " and orderId: " + productReservedEvent.getOrderId());
        cancelProductReservation(productReservedEvent, "Payment timeout occurred. Cancelling the product reservation...");
    }

}
