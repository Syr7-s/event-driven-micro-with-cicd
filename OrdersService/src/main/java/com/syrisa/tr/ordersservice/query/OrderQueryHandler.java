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
    public List<OrderRestModel> findOrderById(String orderId){
        return ordersRepository.findAll().stream().map(order -> {
            OrderRestModel orderRestModel = new OrderRestModel();
            BeanUtils.copyProperties(order,orderRestModel);
            return orderRestModel;
        }).collect(Collectors.toList());
    }
}
