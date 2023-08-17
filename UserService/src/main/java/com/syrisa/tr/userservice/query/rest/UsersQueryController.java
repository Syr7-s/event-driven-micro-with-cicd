package com.syrisa.tr.userservice.query.rest;

import com.syrisa.tr.core.model.User;
import com.syrisa.tr.core.query.FetchUserPaymentDetailsQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UsersQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/{userId}/payment-details")
    public User getUserPaymentDetails(@PathVariable String userId) {
        return queryGateway.query(new FetchUserPaymentDetailsQuery(userId), ResponseTypes.instanceOf(User.class)).join();
    }

}
