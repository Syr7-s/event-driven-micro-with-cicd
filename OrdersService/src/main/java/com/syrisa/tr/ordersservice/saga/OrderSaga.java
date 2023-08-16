package com.syrisa.tr.ordersservice.saga;

import com.syrisa.tr.ordersservice.core.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

@Saga
@RequiredArgsConstructor
public class OrderSaga {

    private final transient CommandGateway commandGateway;

    @StartSaga // This annotation is used to start the saga.
    @SagaEventHandler(associationProperty = "orderId") // This annotation is used to define the event that will be handled by the saga.
    public void handle(OrderCreatedEvent orderCreatedEvent) {

    }
}
