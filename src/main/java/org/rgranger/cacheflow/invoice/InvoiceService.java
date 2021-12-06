package org.rgranger.cacheflow.invoice;

import org.rgranger.cacheflow.invoice.domain.Invoice;
import org.rgranger.cacheflow.invoice.domain.InvoiceStatus;
import org.rgranger.cacheflow.invoice.domain.LineItem;
import org.rgranger.cacheflow.invoice.exception.InvalidInvoiceException;
import org.rgranger.cacheflow.invoice.exception.InvalidLineItemException;
import org.rgranger.cacheflow.invoice.exception.InvalidRequestException;
import org.rgranger.cacheflow.invoice.model.InvoiceModel;
import org.rgranger.cacheflow.invoice.model.LineItemModel;
import org.rgranger.cacheflow.invoice.repository.InvoiceRepo;
import org.rgranger.cacheflow.invoice.repository.LineItemRepo;
import org.rgranger.cacheflow.invoice.resource.InvoiceResource;
import org.rgranger.cacheflow.invoice.resource.LineItemResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private final InvoiceRepo invoiceRepo;
    private final LineItemRepo lineItemRepo;

    public InvoiceService(InvoiceRepo invoiceRepo,
                          LineItemRepo lineItemRepo) {
        this.invoiceRepo = invoiceRepo;
        this.lineItemRepo = lineItemRepo;
    }

    public Long create(InvoiceResource invoiceCreateResource) {
        validateInvoiceCreateRequest(invoiceCreateResource);
        InvoiceModel invoiceModel = invoiceRepo.save(new InvoiceModel(invoiceCreateResource));
        return invoiceModel.getInvoiceId();
    }

    public Long update(Long invoiceId, InvoiceResource invoiceUpdateResource) {
        validateInvoiceUpdateRequest(invoiceUpdateResource);
        Optional<InvoiceModel> invoiceModel = invoiceRepo.findById(invoiceId);
        if (invoiceModel.isPresent()) {
            InvoiceModel updatedInvoiceModel = invoiceModel.get();
            if (invoiceUpdateResource.getCustomerEmail() != null) {
                updatedInvoiceModel.setCustomerEmail(invoiceUpdateResource.getCustomerEmail());
            }
            if (invoiceUpdateResource.getCustomerName() != null) {
                updatedInvoiceModel.setCustomerName(invoiceUpdateResource.getCustomerName());
            }
            if (invoiceUpdateResource.getDescription() != null) {
                updatedInvoiceModel.setDescription(invoiceUpdateResource.getDescription());
            }
            if (invoiceUpdateResource.getDueDate() != null) {
                updatedInvoiceModel.setDueDate(invoiceUpdateResource.getDueDate());
            }
            if (invoiceUpdateResource.getStatus() != null) {
                updatedInvoiceModel.setStatus(invoiceUpdateResource.getStatus());
            }
            if (invoiceUpdateResource.getTotal() != null) {
                updatedInvoiceModel.setTotal(invoiceUpdateResource.getTotal());
            }
            invoiceRepo.save(updatedInvoiceModel);

            return invoiceId;
        } else {
            throw new InvalidInvoiceException(invoiceId);
        }
    }

    public Long createLineItem(Long invoiceId, LineItemResource lineItemResource) {
        Optional<InvoiceModel> invoiceModel = invoiceRepo.findById(invoiceId);
        if (invoiceModel.isPresent()) {
            validateLineItemCreateRequest(lineItemResource);
            LineItemModel lineItemModel = lineItemRepo.save(new LineItemModel(invoiceId, lineItemResource));

            return lineItemModel.getLineItemId();
        } else {
            throw new InvalidInvoiceException(invoiceId);
        }
    }

    public void deleteLineItem(Long invoiceId, Long lineItemId) {
        Optional<LineItemModel> lineItemModel = lineItemRepo.findById(lineItemId);
        if (lineItemModel.isPresent()) {
            lineItemRepo.delete(lineItemModel.get());
        } else {
            throw new InvalidLineItemException(invoiceId, lineItemId);
        }
    }

    public List<Invoice> getInvoices(InvoiceStatus invoiceStatus) {
        List<InvoiceModel> invoiceModels = invoiceRepo.findByStatus(invoiceStatus.name());

        return invoiceModels.stream().map(invoiceModel -> {
            List<LineItemModel> lineItemModels = lineItemRepo.findByInvoiceId(invoiceModel.getInvoiceId());
            Invoice invoice = invoiceModel.toDomain();
            List<LineItem> lineItems = lineItemModels.stream()
                    .map(LineItemModel::toDomain)
                    .collect(Collectors.toList());
            invoice.setLineItems(lineItems);
            return invoice;
        }).collect(Collectors.toList());
    }

    private void validateInvoiceCreateRequest(InvoiceResource invoiceResource) {
        if (invoiceResource.getCustomerEmail() == null) {
            throw new InvalidRequestException("Customer email is required.");
        }
        if (invoiceResource.getCustomerName() == null) {
            throw new InvalidRequestException("Customer name is required.");
        }
        if (invoiceResource.getDescription() == null) {
            throw new InvalidRequestException("Description is required.");
        }
        if (invoiceResource.getDueDate() == null) {
            throw new InvalidRequestException("Due date is required.");
        }
        if (invoiceResource.getStatus() == null) {
            throw new InvalidRequestException("Status is required.");
        }
        if (invoiceResource.getTotal() == null) {
            throw new InvalidRequestException("Total is required.");
        }
    }

    private void validateInvoiceUpdateRequest(InvoiceResource invoiceResource) {
        if (invoiceResource.getCustomerEmail() == null &&
                invoiceResource.getCustomerName() == null &&
                invoiceResource.getDescription() == null &&
                invoiceResource.getDueDate() == null &&
                invoiceResource.getStatus() == null &&
                invoiceResource.getTotal() == null) {
            throw new InvalidRequestException("Invoice update request is invalid. At least one field is required.");
        }
    }

    private void validateLineItemCreateRequest(LineItemResource lineItemResource) {
        if (lineItemResource.getLineItem() == null) {
            throw new InvalidRequestException("Line item is required.");
        }
    }
}
