package com.syrisa.tr.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class PaymentDetails implements Serializable {
    private final String name;
    private final String cardNumber;
    private final int validUntilMonth;
    private final int validUntilYear;
    private final String cvv;
}
