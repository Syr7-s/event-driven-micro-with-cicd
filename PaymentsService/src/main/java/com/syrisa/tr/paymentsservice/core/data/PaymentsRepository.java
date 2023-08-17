package com.syrisa.tr.paymentsservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository  extends JpaRepository<PaymentEntity, String> {
}
