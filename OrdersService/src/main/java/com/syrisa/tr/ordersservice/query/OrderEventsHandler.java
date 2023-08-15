package com.syrisa.tr.ordersservice.query;

import com.syrisa.tr.ordersservice.core.entity.OrderEntity;
import com.syrisa.tr.ordersservice.core.entity.OrdersRepository;
import com.syrisa.tr.ordersservice.core.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
@RequiredArgsConstructor
public class OrderEventsHandler {
    private final OrdersRepository ordersRepository;

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        System.out.println("Illegal Argument Exception: " + exception.getMessage());
    }

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderCreatedEvent, orderEntity);
        try {
            ordersRepository.save(orderEntity);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

    }
}
