package com.syrisa.tr.ordersservice.command.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrdersCommandController {
    @PostMapping
    public String createOrder(){
        return "HTTP Post Handled";
    }
}
