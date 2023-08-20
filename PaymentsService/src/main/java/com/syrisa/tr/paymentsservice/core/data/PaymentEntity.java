package com.syrisa.tr.paymentsservice.core.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class PaymentEntity implements Serializable {
    private static final long serialVersionUID = 5313493413859894403L;
    @Id
    private String paymentId;
    @Column
    public String orderId;
}
