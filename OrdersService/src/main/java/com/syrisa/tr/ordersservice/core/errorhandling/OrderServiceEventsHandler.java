package com.syrisa.tr.ordersservice.core.errorhandling;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

import javax.annotation.Nonnull;

public class OrderServiceEventsHandler implements ListenerInvocationErrorHandler{
    @Override
    public void onError(@Nonnull Exception e,@Nonnull EventMessage<?> eventMessage, @Nonnull EventMessageHandler eventMessageHandler) throws Exception {
        throw e;
    }
}
