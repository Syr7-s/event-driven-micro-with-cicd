package com.syrisa.tr.paymentsservice.command;

import com.syrisa.tr.core.commands.ProcessPaymentCommand;
import com.syrisa.tr.core.events.PaymentProcessedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Aggregate
@NoArgsConstructor
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;

    private String orderId;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentAggregate.class);


    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {

        if (processPaymentCommand.getPaymentDetails() == null) {
            throw new IllegalArgumentException("Payment details must not be null");
        }

        if (processPaymentCommand.getOrderId() == null || processPaymentCommand.getOrderId().isBlank()) {
            throw new IllegalArgumentException("Order id must not be null or blank");
        }

        if (processPaymentCommand.getPaymentId() == null || processPaymentCommand.getPaymentId().isBlank()) {
            throw new IllegalArgumentException("Payment id must not be null or blank");
        }
        LOGGER.info("ProcessPaymentCommand is called for orderId: " + processPaymentCommand.getOrderId() + " and paymentId: " + processPaymentCommand.getPaymentId());
        PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder()
                        .paymentId(processPaymentCommand.getPaymentId())
                        .orderId(processPaymentCommand.getOrderId())
                        .build();

        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    @EventSourcingHandler
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getOrderId();
    }
}
