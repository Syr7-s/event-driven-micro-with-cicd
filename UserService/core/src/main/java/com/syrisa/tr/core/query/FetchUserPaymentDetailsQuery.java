package com.syrisa.tr.core.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class FetchUserPaymentDetailsQuery {
    //@TargetAggregateIdentifier
    private String userId;
}
