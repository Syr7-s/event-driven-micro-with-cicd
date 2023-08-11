package com.syrisa.tr.productsservice.rest;

import com.syrisa.tr.productsservice.command.CreateProductCommand;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductsController {

    private final Environment environment;

    private final CommandGateway commandGateway;


    @PostMapping
    public String createProduct(@RequestBody CreateProductRestModel createProductRestModel){
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .price(createProductRestModel.getPrice())
                .productId(UUID.randomUUID().toString())
                .quantity(createProductRestModel.getQuantity())
                .title(createProductRestModel.getTitle())
                .build();
        String returnValue = "" ;
        try {
            returnValue = commandGateway.sendAndWait(createProductCommand);
        }catch (Exception e){
            returnValue = e.getLocalizedMessage();
        }
        return returnValue;
    }

    @GetMapping
    public String getProduct(){
        return "HTTP Get Handled "+ environment.getProperty("local.server.port");
    }

    @PutMapping
    public String updateProduct(){
        return "HTTP Put Handled";
    }

    @DeleteMapping
    public String deleteProduct(){
        return "HTTP Delete Handled";
    }
}
