package com.syrisa.tr.ordersservice.command.rest;

import com.syrisa.tr.ordersservice.command.CreateOrderCommand;
import com.syrisa.tr.ordersservice.core.utils.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
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

    @PostMapping
    public String createOrder(@Valid @RequestBody CreateOrderRestModel createOrderRestModel){
        String returnValue = "";
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                .productId(createOrderRestModel.getProductId())
                .quantity(createOrderRestModel.getQuantity())
                .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                .addressId(createOrderRestModel.getAddressId())
                .orderStatus(OrderStatus.CREATED)
                .build();
        returnValue = commandGateway.sendAndWait(createOrderCommand);
        return returnValue;
    }
}
