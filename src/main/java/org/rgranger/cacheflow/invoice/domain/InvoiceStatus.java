package org.rgranger.cacheflow.invoice.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.rgranger.cacheflow.invoice.exception.InvalidRequestException;

public enum InvoiceStatus {
    draft,
    approved,
    sent,
    paid;

    @JsonSetter
    public static InvoiceStatus of(String status) {
        for (InvoiceStatus invoiceStatus : InvoiceStatus.values()) {
            if (invoiceStatus.name().equalsIgnoreCase(status)) {
                return invoiceStatus;
            }
        }

        throw new InvalidRequestException(status + " is an invalid invoice status");
    }
}
