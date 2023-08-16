package com.syrisa.tr.ordersservice.core.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "order_lookup")
public class OrderLookupEntity {
    private static final long serialVersionUID = 1234567891L;
    @Id
    private String orderId;

}
