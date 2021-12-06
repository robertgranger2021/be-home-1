package org.rgranger.cacheflow.invoice;

import org.rgranger.cacheflow.invoice.domain.Invoice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

public class ResponseEntityFactory {
    public static ResponseEntity<Void> createResponse(Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION,
                        ServletUriComponentsBuilder.fromCurrentRequestUri()
                                .pathSegment("{id}")
                                .buildAndExpand(id)
                                .toUriString()
                ).build();
    }

    public static ResponseEntity<Void> updateResponse() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public static ResponseEntity<Void> deleteResponse() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public static ResponseEntity<List<Invoice>> getResponse(List<Invoice> invoices) {
        return ResponseEntity.status(HttpStatus.OK).body(invoices);
    }
}
