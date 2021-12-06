package org.rgranger.cacheflow.invoice.repository;

import org.rgranger.cacheflow.invoice.model.InvoiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepo extends JpaRepository<InvoiceModel, Long> {
    Optional<InvoiceModel> findById(Long id);

    List<InvoiceModel> findByStatus(String status);
}
