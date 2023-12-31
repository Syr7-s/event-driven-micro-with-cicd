package com.syrisa.tr.ordersservice.query;

import com.syrisa.tr.ordersservice.core.entity.OrderEntity;
import com.syrisa.tr.ordersservice.core.entity.OrdersRepository;
import com.syrisa.tr.ordersservice.core.model.OrderSummary;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OrderQueryHandler {
    private final OrdersRepository ordersRepository;
/*
    @QueryHandler
    public List<OrderRestModel> findOrders(FindOrdersQuery findOrdersQuery) {
        return ordersRepository.findAll().stream().map(order -> {
            OrderRestModel orderRestModel = new OrderRestModel();
            BeanUtils.copyProperties(order, orderRestModel);
            return orderRestModel;
        }).collect(Collectors.toList());
    }
*/
    @QueryHandler
    public OrderSummary findOrderById(FindOrdersQuery findOrdersQuery){
        OrderEntity orderEntity = ordersRepository.findByOrderId(findOrdersQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(), "");
    }

}

