package org.rgranger.cacheflow.invoice.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.rgranger.cacheflow.invoice.domain.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceResource {
    @JsonProperty("customer_email")
    private String customerEmail;

    @JsonProperty("customer_name")
    private String customerName;

    private String description;

    @JsonProperty("due_date")
    private LocalDate dueDate;

    private InvoiceStatus status;

    private BigDecimal total;

    public InvoiceResource() {
    }

    public InvoiceResource(String customerEmail,
                           String customerName,
                           String description,
                           LocalDate dueDate,
                           InvoiceStatus status,
                           BigDecimal total) {
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.total = total;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
