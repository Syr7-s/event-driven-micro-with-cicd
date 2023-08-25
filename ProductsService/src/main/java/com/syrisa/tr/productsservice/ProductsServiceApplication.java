package com.syrisa.tr.productsservice;

import com.syrisa.tr.core.commands.ReserveProductCommand;
import com.syrisa.tr.productsservice.command.interceptor.CreateProductCommandInterceptor;
import com.syrisa.tr.productsservice.core.errorhandling.ProductsServiceEventsHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@EnableJpaRepositories
@EnableDiscoveryClient // This annotation is used to register the service in the Eureka server.
@Import({ com.syrisa.tr.productsservice.configuration.AxonXStreamConfig.class })
public class ProductsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsServiceApplication.class, args);

    }
    @Autowired
    public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandBus){
       commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer){
        configurer.registerListenerInvocationErrorHandler("product-group",conf -> new ProductsServiceEventsHandler());
        // configurer.registerListenerInvocationErrorHandler("product-group",conf -> PropagatingErrorHandler.instance());
    }

    @Bean(name="productSnapshotTriggerDefinition")
    public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter){
        return new EventCountSnapshotTriggerDefinition(snapshotter,3);
    }

}
