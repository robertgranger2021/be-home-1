package org.rgranger.cacheflow.invoice.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class LineItemResource {
    @JsonProperty("line_item")
    private String lineItem;

    private BigDecimal cost;

    public LineItemResource() {
    }

    public LineItemResource(String lineItem, BigDecimal cost) {
        this.lineItem = lineItem;
        this.cost = cost;
    }

    public String getLineItem() {
        return lineItem;
    }

    public void setLineItem(String lineItem) {
        this.lineItem = lineItem;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
