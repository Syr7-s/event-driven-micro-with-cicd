package com.syrisa.tr.ordersservice.core.errorhandling;
import org.apache.http.HttpStatus;
import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class OrderServiceErrorHandler {
    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage,new HttpHeaders(),HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherStateException(IllegalStateException e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage,new HttpHeaders(),HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value={CommandExecutionException.class})
    public ResponseEntity<Object> handleCommandExecutionException(Exception e, WebRequest webRequest){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

}
