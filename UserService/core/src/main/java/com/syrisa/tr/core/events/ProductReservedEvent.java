package com.syrisa.tr.core.events;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class ProductReservedEvent {
    private final String productId;
    private final int quantity;
    private final String orderId;
    private final String userId;
}
