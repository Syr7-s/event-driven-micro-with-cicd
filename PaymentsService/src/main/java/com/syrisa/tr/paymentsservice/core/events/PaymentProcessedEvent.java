package com.syrisa.tr.paymentsservice.core.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentProcessedEvent {
    private  String paymentId;
    private  String orderId;
}
