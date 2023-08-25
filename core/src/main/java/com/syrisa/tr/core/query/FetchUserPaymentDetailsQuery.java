package com.syrisa.tr.core.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchUserPaymentDetailsQuery {
    @TargetAggregateIdentifier
    private String userId;
}
