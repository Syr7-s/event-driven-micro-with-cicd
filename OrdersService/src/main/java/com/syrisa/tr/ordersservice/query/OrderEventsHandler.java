package com.syrisa.tr.ordersservice.query;

import com.syrisa.tr.ordersservice.core.entity.OrderEntity;
import com.syrisa.tr.ordersservice.core.entity.OrdersRepository;
import com.syrisa.tr.ordersservice.core.events.OrderApprovedEvent;
import com.syrisa.tr.ordersservice.core.events.OrderCreatedEvent;
import com.syrisa.tr.ordersservice.core.events.OrderRejectedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
@RequiredArgsConstructor
public class OrderEventsHandler {
    private final OrdersRepository ordersRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventsHandler.class);


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
            LOGGER.info("OrderCreatedEvent is called for orderId: from EventsHandler " + orderCreatedEvent.getOrderId());

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

    }

    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());
        if (orderEntity == null) {
            return;
        }
        orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());
        ordersRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(orderRejectedEvent.getOrderId());
        orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
        ordersRepository.save(orderEntity);
    }
}
