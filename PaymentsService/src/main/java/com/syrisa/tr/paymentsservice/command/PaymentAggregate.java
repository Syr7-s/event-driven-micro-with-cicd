package com.syrisa.tr.paymentsservice.command;

import com.syrisa.tr.paymentsservice.command.commands.ProcessPaymentCommand;
import com.syrisa.tr.paymentsservice.core.events.PaymentProcessedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
public class PaymentAggregate {
    @AggregateIdentifier
    private  String orderId;
    private  String paymentId;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand){

        if (processPaymentCommand.getOrderId() == null || processPaymentCommand.getOrderId().isBlank()){
            throw new IllegalArgumentException("Order id must not be null or blank");
        }

        if (processPaymentCommand.getPaymentId() == null || processPaymentCommand.getPaymentId().isBlank()){
            throw new IllegalArgumentException("Payment id must not be null or blank");
        }

        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent();
        BeanUtils.copyProperties(processPaymentCommand,paymentProcessedEvent);
        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    @EventSourcingHandler
    public void handle(ProcessPaymentCommand processPaymentCommand){
        this.orderId = processPaymentCommand.getOrderId();
        this.paymentId = processPaymentCommand.getPaymentId();
    }
}
