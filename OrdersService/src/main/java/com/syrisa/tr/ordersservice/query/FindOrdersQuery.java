package com.syrisa.tr.ordersservice.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FindOrdersQuery {
    private String orderId;
    public FindOrdersQuery(String orderId) {
    }
}
