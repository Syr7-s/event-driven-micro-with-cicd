package com.syrisa.tr.core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class PaymentProcessedEvent {
    private final String orderId;
    private final String paymentId;
}
