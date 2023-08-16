package com.syrisa.tr.ordersservice.command;

import com.syrisa.tr.ordersservice.core.entity.OrderLookupEntity;
import com.syrisa.tr.ordersservice.core.entity.OrdersLookupRepository;
import com.syrisa.tr.ordersservice.core.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
@RequiredArgsConstructor
public class OrderLookupEventsHandler {
    private final OrdersLookupRepository orderLookupRepository;
    @EventHandler
    public void on(OrderCreatedEvent event){
        OrderLookupEntity orderLookupEntity = new OrderLookupEntity(event.getOrderId());
        orderLookupRepository.save(orderLookupEntity);
    }
}
