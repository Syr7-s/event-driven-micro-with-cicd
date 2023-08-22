package com.syrisa.tr.core.commands;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
@XStreamAlias("ReserveProductCommand")
public class ReserveProductCommand implements Serializable {
    private static final long serialVersionUID = 4L;


    @TargetAggregateIdentifier
    private final String productId;
    private final int quantity;
    private final String orderId;
    private final String userId;

}
