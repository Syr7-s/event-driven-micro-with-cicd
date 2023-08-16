package com.syrisa.tr.ordersservice.query.rest;

import com.syrisa.tr.ordersservice.core.utils.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRestModel {
    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
