package org.rgranger.cacheflow.invoice.model;

import org.rgranger.cacheflow.invoice.domain.LineItem;
import org.rgranger.cacheflow.invoice.resource.LineItemResource;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "line_item")
public class LineItemModel {
    @Id
    @GeneratedValue
    @Column(name = "id_line_item", columnDefinition = "int")
    private Long lineItemId;

    @Column(name = "invoice_id_invoice_fk", columnDefinition = "int")
    private Long invoiceId;

    @Column(name = "line_item_description", columnDefinition = "varchar(255)")
    private String lineItemDescription;

    @Column(name = "cost", columnDefinition = "decimal(10,2)")
    private BigDecimal cost;

    public LineItemModel() {
    }

    public LineItemModel(Long invoiceId, LineItemResource lineItemResource) {
        this.invoiceId = invoiceId;
        this.lineItemDescription = lineItemResource.getLineItem();
        this.cost = lineItemResource.getCost();
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getLineItem() {
        return lineItemDescription;
    }

    public void setLineItem(String lineItem) {
        this.lineItemDescription = lineItem;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LineItem toDomain() {
        LineItem lineItem = new LineItem();
        lineItem.setLineItemId(lineItemId);
        lineItem.setLineItem(lineItemDescription);
        lineItem.setInvoiceId(invoiceId);
        lineItem.setCost(cost);

        return lineItem;
    }
}
