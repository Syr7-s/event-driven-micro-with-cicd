package com.syrisa.tr.core.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentProcessedEvent {
    private final String orderId;
    private final String paymentId;
}
