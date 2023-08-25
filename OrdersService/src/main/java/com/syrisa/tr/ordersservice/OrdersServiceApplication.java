package com.syrisa.tr.ordersservice;

import com.syrisa.tr.core.commands.ReserveProductCommand;
import com.thoughtworks.xstream.XStream;
import org.axonframework.config.Configuration;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"com.syrisa.tr"})
@EnableDiscoveryClient // It is used to register the service to the Eureka server.
@Import({ com.syrisa.tr.ordersservice.configuration.AxonXStreamConfig.class })
public class OrdersServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(OrdersServiceApplication.class, args);

    }

    @Bean
    public DeadlineManager deadlineManager(Configuration configuration,SpringTransactionManager springTransactionManager){
        return SimpleDeadlineManager.builder()
                .scopeAwareProvider(new ConfigurationScopeAwareProvider(configuration))
                .transactionManager(springTransactionManager)
                .build();

    }

}
