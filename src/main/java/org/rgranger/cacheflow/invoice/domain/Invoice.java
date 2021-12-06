package org.rgranger.cacheflow.invoice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.rgranger.cacheflow.invoice.resource.InvoiceResource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Invoice extends InvoiceResource {
    private Long id;

    @JsonProperty("line_items")
    private List<LineItem> lineItems;

    public Invoice() {
    }

    public Invoice(InvoiceResource invoiceResource) {
        super(invoiceResource.getCustomerEmail(),
                invoiceResource.getCustomerName(),
                invoiceResource.getDescription(),
                invoiceResource.getDueDate(),
                invoiceResource.getStatus(),
                invoiceResource.getTotal());
    }

    public Invoice(Long id,
                   String customerEmail,
                   String customerName,
                   String description,
                   LocalDate dueDate,
                   InvoiceStatus status,
                   BigDecimal total,
                   List<LineItem> lineItems) {
        super(customerEmail, customerName, description, dueDate, status, total);
        this.id = id;
        this.lineItems = lineItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
