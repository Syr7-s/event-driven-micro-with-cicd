package com.syrisa.tr.ordersservice.core.entity;

import com.syrisa.tr.ordersservice.core.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 5313493413859894403L;
    @Id
    @Column(unique = true)
    public String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
