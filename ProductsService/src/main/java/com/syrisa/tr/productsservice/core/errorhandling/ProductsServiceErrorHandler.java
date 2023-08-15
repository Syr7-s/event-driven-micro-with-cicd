package com.syrisa.tr.productsservice.core.errorhandling;

import org.apache.http.HttpStatus;
import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

@ControllerAdvice
public class ProductsServiceErrorHandler {
    @ExceptionHandler(value={IllegalStateException.class})
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException e, WebRequest webRequest){
        ErrorMessage errorMessage = new ErrorMessage(LocalDate.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<Object> handleOtherStateException(Exception e, WebRequest webRequest){
        ErrorMessage errorMessage = new ErrorMessage(LocalDate.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value={CommandExecutionException.class})
    public ResponseEntity<Object> handleCommandExecutionException(Exception e, WebRequest webRequest){
        ErrorMessage errorMessage = new ErrorMessage(LocalDate.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
