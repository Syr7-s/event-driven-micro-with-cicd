package com.syrisa.tr.productsservice.command.rest;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CreateProductRestModel {
    @NotBlank(message = "Product title is a required field")
    private String title;
    @Min(value = 1,message = "Product quantity cannot be less than 1")
    @Max(value = 5,message = "Product quantity cannot be greater than 100")
    private Integer quantity;
    @Min(value = 1,message = "Product price cannot be less than 1")
    private BigDecimal price;
}
