package com.syrisa.tr.ordersservice.command.rest;

import com.syrisa.tr.ordersservice.command.commands.CreateOrderCommand;
import com.syrisa.tr.ordersservice.core.model.OrderStatus;
import com.syrisa.tr.ordersservice.core.model.OrderSummary;
import com.syrisa.tr.ordersservice.query.FindOrdersQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrdersCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersCommandController.class);


    @PostMapping
    public OrderSummary createOrder(@Valid @RequestBody CreateOrderRestModel createOrderRestModel){
        String returnValue = "";
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                .productId(createOrderRestModel.getProductId())
                .quantity(createOrderRestModel.getQuantity())
                .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                .addressId(createOrderRestModel.getAddressId())
                .orderStatus(OrderStatus.CREATED)
                .build();

        SubscriptionQueryResult<OrderSummary,OrderSummary> queryResult = queryGateway.subscriptionQuery(
                new FindOrdersQuery(createOrderCommand.getOrderId()),
                ResponseTypes.instanceOf(OrderSummary.class),
                ResponseTypes.instanceOf(OrderSummary.class)
        );
        try {
            LOGGER.info("OrderCommandController-createOrder() -> CreateOrderCommand is sending...");
            commandGateway.sendAndWait(createOrderCommand);
            LOGGER.info("OrderCommandController-createOrder() -> CreateOrderCommand is sent.");
            OrderSummary orderSummary = queryResult.updates().blockFirst();
            LOGGER.info("OrderSummary is returned. %s",orderSummary.toString());
            return  orderSummary;
        }finally {
            queryResult.close();
        }
    }
}
