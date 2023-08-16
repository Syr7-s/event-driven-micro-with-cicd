package com.syrisa.tr.ordersservice.query;

import com.syrisa.tr.ordersservice.core.entity.OrdersRepository;
import com.syrisa.tr.ordersservice.query.rest.OrderRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderQueryHandler {
    private final OrdersRepository ordersRepository;

    @QueryHandler
    public List<OrderRestModel> findOrders(FindOrdersQuery findOrdersQuery) {
        return ordersRepository.findAll().stream().map(order -> {
            OrderRestModel orderRestModel = new OrderRestModel();
            BeanUtils.copyProperties(order, orderRestModel);
            return orderRestModel;
        }).collect(Collectors.toList());
    }
    /*
    @QueryHandler
    public OrderRestModel findOrderById(FindOrdersQuery findOrdersQuery){
        return ordersRepository.findById(findOrdersQuery.getOrderId()).map(order -> {
            OrderRestModel orderRestModel = new OrderRestModel();
            BeanUtils.copyProperties(order,orderRestModel);
            return orderRestModel;
        }).orElseThrow(()->new IllegalArgumentException("Order not found!"));
    }
*/
}

