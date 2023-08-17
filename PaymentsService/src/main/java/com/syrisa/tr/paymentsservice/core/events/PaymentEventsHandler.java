package com.syrisa.tr.paymentsservice.core.events;

import com.syrisa.tr.core.events.PaymentProcessedEvent;
import com.syrisa.tr.paymentsservice.core.data.PaymentEntity;
import com.syrisa.tr.paymentsservice.core.data.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ProcessingGroup("payment-group")
@RequiredArgsConstructor
public class PaymentEventsHandler {
    private final PaymentsRepository paymentsRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEventsHandler.class);


    @ExceptionHandler
    public void on(PaymentProcessedEvent event) {
        PaymentEntity paymentEntity = new PaymentEntity();
        BeanUtils.copyProperties(event, paymentEntity);
        try {
            paymentsRepository.save(paymentEntity);
            LOGGER.info("PaymentProcessedEvent is called for orderId: from EventsHandler " + event.getOrderId());

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
}
