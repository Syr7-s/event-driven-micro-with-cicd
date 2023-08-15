package com.syrisa.tr.ordersservice.core.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface OrdersRepository extends JpaRepository<OrderEntity, String> {
}
