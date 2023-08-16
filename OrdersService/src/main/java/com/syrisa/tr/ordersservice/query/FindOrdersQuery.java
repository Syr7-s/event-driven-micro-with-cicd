package com.syrisa.tr.ordersservice.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Value
public class FindOrdersQuery {
    private final  String orderId;

}
