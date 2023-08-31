package com.syrisa.tr.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
public class CancelProductReservationCommand  {

    @TargetAggregateIdentifier
    private final String productId;

    private final int quantity;
    private final String orderId;
    private final String userId;
    private final String reason;
}
