package org.rgranger.cacheflow.invoice.exception;

public class InvalidLineItemException extends RuntimeException {
    public InvalidLineItemException(Long invoiceId, Long lineItemId) {
        super("The line item id " + lineItemId + " does not exist for invoice id " + invoiceId);
    }
}
