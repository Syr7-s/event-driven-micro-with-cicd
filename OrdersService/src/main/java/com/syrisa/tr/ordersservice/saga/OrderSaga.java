package com.syrisa.tr.ordersservice.saga;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.spring.stereotype.Saga;

@Saga
@RequiredArgsConstructor
public class OrderSaga {

    private final transient CommandGateway commandGateway;

}
