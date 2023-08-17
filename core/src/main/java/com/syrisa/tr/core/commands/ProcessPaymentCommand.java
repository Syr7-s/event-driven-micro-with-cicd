package com.syrisa.tr.core.commands;

import com.syrisa.tr.core.model.PaymentDetails;
import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Builder
public class ProcessPaymentCommand {
    @TargetAggregateIdentifier
    private final String paymentId;
    private final String orderId;
    private final PaymentDetails paymentDetails;
}
