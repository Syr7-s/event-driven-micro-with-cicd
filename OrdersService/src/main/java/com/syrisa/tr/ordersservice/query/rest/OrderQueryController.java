package com.syrisa.tr.ordersservice.query.rest;

import com.syrisa.tr.ordersservice.query.FindOrdersQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderQueryController {
    private final QueryGateway queryGateway;

    @GetMapping
    public List<OrderRestModel> getOrders() {
        //return queryGateway.query(new FindOrdersQuery(), ResponseTypes.multipleInstancesOf(OrderRestModel.class)).join();
        return null;
    }
/*
    @GetMapping("/{orderId}")
    public OrderRestModel getOrderById(@PathVariable String orderId) {
        return queryGateway.query(new FindOrdersQuery(orderId), ResponseTypes.instanceOf(OrderRestModel.class)).join();
    }*/
}
