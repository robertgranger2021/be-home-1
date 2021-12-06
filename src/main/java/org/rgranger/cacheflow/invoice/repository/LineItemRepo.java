package org.rgranger.cacheflow.invoice.repository;

import org.rgranger.cacheflow.invoice.model.LineItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LineItemRepo extends JpaRepository<LineItemModel, Long> {
    Optional<LineItemModel> findById(Long id);

    List<LineItemModel> findByInvoiceId(Long invoiceId);
}
