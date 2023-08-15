package com.syrisa.tr.productsservice.core.errorhandling;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private LocalDate timestamp;
    private String message;
}
