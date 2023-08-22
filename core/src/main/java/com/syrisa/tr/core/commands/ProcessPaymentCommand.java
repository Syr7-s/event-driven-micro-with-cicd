package com.syrisa.tr.core.commands;

import com.syrisa.tr.core.model.PaymentDetails;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
public class ProcessPaymentCommand implements Serializable {

    private static final long serialVersionUID = 3L;

    @TargetAggregateIdentifier
    private final String paymentId;
    private final String orderId;
    private final PaymentDetails paymentDetails;
}
