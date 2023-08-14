package com.syrisa.tr.productsservice.core.errorhandling;

import org.apache.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ProductsServiceErrorHandler {
    @ExceptionHandler(value={IllegalStateException.class})
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException e, WebRequest webRequest){
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<Object> handleOtherStateException(Exception e, WebRequest webRequest){
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
