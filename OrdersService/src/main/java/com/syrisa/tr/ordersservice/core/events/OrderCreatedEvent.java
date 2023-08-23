package com.syrisa.tr.ordersservice.core.events;

import com.syrisa.tr.ordersservice.core.model.OrderStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
