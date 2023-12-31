package com.syrisa.tr.core.commands;

import com.syrisa.tr.core.model.PaymentDetails;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
@Builder
@Data
public class ProcessPaymentCommand {


    @TargetAggregateIdentifier
    private final String paymentId;

    private final String orderId;
    private final PaymentDetails paymentDetails;
}
