package com.faizalsidek.inventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StockAcquisition.
 */
@Entity
@Table(name = "stock_acquisition")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stockacquisition")
public class StockAcquisition extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "invoice_id")
    private String invoiceId;
    
    @NotNull
    @Column(name = "date_acquire", nullable = false)
    private LocalDate dateAcquire;
    
    @Column(name = "remarks")
    private String remarks;
    
    @OneToMany(mappedBy = "stockAcquisition")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Item> items = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
    
    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LocalDate getDateAcquire() {
        return dateAcquire;
    }
    
    public void setDateAcquire(LocalDate dateAcquire) {
        this.dateAcquire = dateAcquire;
    }

    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockAcquisition stockAcquisition = (StockAcquisition) o;
        if(stockAcquisition.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockAcquisition.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockAcquisition{" +
            "id=" + id +
            ", invoiceId='" + invoiceId + "'" +
            ", dateAcquire='" + dateAcquire + "'" +
            ", remarks='" + remarks + "'" +
            '}';
    }
}
