package org.rgranger.cacheflow.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.rgranger.cacheflow.invoice.resource.LineItemResource;

import java.math.BigDecimal;

public class LineItem extends LineItemResource {
    @JsonProperty("line_item_id")
    private Long lineItemId;

    @JsonIgnore
    private Long invoiceId;

    public LineItem() {
    }

    public LineItem(Long invoiceId, LineItemResource lineItemResource) {
        super(lineItemResource.getLineItem(), lineItemResource.getCost());
        this.invoiceId = invoiceId;
    }
    public LineItem(Long invoiceId, Long lineItemId, String lineItem, BigDecimal cost) {
        super(lineItem, cost);
        this.invoiceId = invoiceId;
        this.lineItemId = lineItemId;
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
}
