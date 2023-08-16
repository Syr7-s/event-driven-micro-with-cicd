package com.syrisa.tr.ordersservice.command.interceptor;

import com.syrisa.tr.ordersservice.command.commands.CreateOrderCommand;
import com.syrisa.tr.ordersservice.core.entity.OrderLookupEntity;
import com.syrisa.tr.ordersservice.core.entity.OrdersLookupRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class CreateOrderCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private final OrdersLookupRepository ordersLookupRepository;

    public static  final Logger LOGGER = Logger.getLogger(CreateOrderCommandInterceptor.class.getName());


    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> list) {
        return (index, command) -> {
            LOGGER.info(String.format("Intercepted command %s",  command.getPayloadType()));
            if(CreateOrderCommand.class.equals(command.getPayloadType())){
                CreateOrderCommand createOrderCommand = (CreateOrderCommand) command.getPayload();
                OrderLookupEntity orderLookupEntity = ordersLookupRepository.findByOrderId(createOrderCommand.getOrderId());
                if(orderLookupEntity != null){
                    throw new IllegalStateException(String.format("Order with orderId %s already exists", createOrderCommand.getOrderId()));
                }
            }
            return command;
        };
    }
}
