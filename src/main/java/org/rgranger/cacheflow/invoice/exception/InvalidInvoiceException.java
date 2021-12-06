package org.rgranger.cacheflow.invoice.exception;

public class InvalidInvoiceException extends RuntimeException {
    public InvalidInvoiceException(Long invoiceId) {
        super("The invoice id " + invoiceId + " does not exist");
    }
}
