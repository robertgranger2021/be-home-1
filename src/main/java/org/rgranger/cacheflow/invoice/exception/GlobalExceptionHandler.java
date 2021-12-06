package org.rgranger.cacheflow.invoice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    ResponseEntity<String> handleInvalidRequest(InvalidRequestException invalidRequestException) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(invalidRequestException.getMessage());
    }

    @ExceptionHandler(InvalidInvoiceException.class)
    ResponseEntity<String> handleInvalidInvoice(InvalidInvoiceException invalidInvoiceException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(invalidInvoiceException.getMessage());
    }

    @ExceptionHandler(InvalidLineItemException.class)
    ResponseEntity<String> handleInvalidLineItem(InvalidLineItemException invalidLineItemException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(invalidLineItemException.getMessage());
    }
}
