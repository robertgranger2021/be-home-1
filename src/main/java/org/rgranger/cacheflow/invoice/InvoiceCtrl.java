package org.rgranger.cacheflow.invoice;

import org.rgranger.cacheflow.invoice.domain.Invoice;
import org.rgranger.cacheflow.invoice.domain.InvoiceStatus;
import org.rgranger.cacheflow.invoice.resource.InvoiceResource;
import org.rgranger.cacheflow.invoice.resource.LineItemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvoiceCtrl {
    private final InvoiceService invoiceService;

    public InvoiceCtrl(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping(value= "/invoices", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createInvoice(@RequestBody InvoiceResource invoiceResource) {
        Long id = invoiceService.create(invoiceResource);

        return ResponseEntityFactory.createResponse(id);
    }

    @PutMapping(value= "/invoices/{invoiceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateInvoice(@PathVariable Long invoiceId,
                                              @RequestBody InvoiceResource invoiceResource) {
        Long id = invoiceService.update(invoiceId, invoiceResource);

        return ResponseEntityFactory.updateResponse();
    }

    @PostMapping(value= "/invoices/{invoiceId}/line-items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createLineItem(@PathVariable Long invoiceId,
                                               @RequestBody LineItemResource lineItemCreationResource) {
        Long id = invoiceService.createLineItem(invoiceId, lineItemCreationResource);

        return ResponseEntityFactory.createResponse(id);
    }

    @DeleteMapping(value= "/invoices/{invoiceId}/line-items/{lineItemId}")
    public ResponseEntity<Void> deleteLineItem(@PathVariable Long invoiceId,
                                               @PathVariable Long lineItemId) {
        invoiceService.deleteLineItem(invoiceId, lineItemId);

        return ResponseEntityFactory.deleteResponse();
    }

    @GetMapping(value= "/invoices")
    public ResponseEntity<List<Invoice>> getInvoices(@RequestParam("status") String invoiceStatus) {
        List<Invoice> invoices = invoiceService.getInvoices(InvoiceStatus.of(invoiceStatus));

        return ResponseEntityFactory.getResponse(invoices);
    }
}
