package org.rgranger.cacheflow.invoice.model;

import org.rgranger.cacheflow.invoice.domain.Invoice;
import org.rgranger.cacheflow.invoice.domain.InvoiceStatus;
import org.rgranger.cacheflow.invoice.resource.InvoiceResource;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
public class InvoiceModel {
    @Id
    @GeneratedValue
    @Column(name = "id_invoice", columnDefinition = "int")
    private Long invoiceId;

    @Column(name = "customer_email", columnDefinition = "varchar(70)")
    private String customerEmail;

    @Column(name = "customer_name", columnDefinition = "varchar(50)")
    private String customerName;

    @Column(name = "description", columnDefinition = "varchar(255)")
    private String description;

    @Column(name = "due_date", columnDefinition = "date")
    private Date dueDate;

    @Column(name = "status", columnDefinition = "varchar(10)")
    private String status;

    @Column(name = "total", columnDefinition = "decimal(10,2)")
    private BigDecimal total;

    public InvoiceModel() {
    }

    public InvoiceModel(InvoiceResource invoiceResource) {
        this.customerEmail = invoiceResource.getCustomerEmail();
        this.customerName = invoiceResource.getCustomerName();
        this.description = invoiceResource.getDescription();
        this.dueDate = Date.valueOf(invoiceResource.getDueDate());
        this.status = invoiceResource.getStatus().toString();
        this.total = invoiceResource.getTotal();
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
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
        return dueDate != null ? dueDate.toLocalDate() : null;
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate != null) {
            this.dueDate = Date.valueOf(dueDate);
        }
    }

    public InvoiceStatus getStatus() {
        return InvoiceStatus.of(status);
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status.name();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Invoice toDomain() {
        Invoice invoice = new Invoice();
        invoice.setCustomerEmail(customerEmail);
        invoice.setCustomerName(customerName);
        invoice.setDescription(description);
        invoice.setId(invoiceId);
        invoice.setDueDate(dueDate.toLocalDate());
        invoice.setStatus(getStatus());
        invoice.setTotal(total);

        return invoice;
    }
}
