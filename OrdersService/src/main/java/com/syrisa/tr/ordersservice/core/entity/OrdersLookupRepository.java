package com.syrisa.tr.ordersservice.core.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersLookupRepository extends JpaRepository<OrderLookupEntity,String> {
}
