package com.syrisa.tr.userservice.query;

import com.syrisa.tr.core.model.PaymentDetails;
import com.syrisa.tr.core.model.User;
import com.syrisa.tr.core.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {

    @QueryHandler
    public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("Tom SAVYER")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        User user = User.builder()
                .firstName("Tom")
                .lastName("SAVYER")
                .userId(query.getUserId())
                .paymentDetails(paymentDetails)
                .build();

        return user;
    }

}
