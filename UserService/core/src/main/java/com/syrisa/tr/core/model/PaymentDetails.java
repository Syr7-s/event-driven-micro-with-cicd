package com.syrisa.tr.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

@Data
@Builder
public class PaymentDetails implements Serializable {
    private final String name;
    private final String cardNumber;
    private final int validUntilMonth;
    private final int validUntilYear;
    private final String cvv;
}
