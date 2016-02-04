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
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "item")
public class Item extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;
    
    @Column(name = "storage_location")
    private String storageLocation;
    
    @NotNull
    @Column(name = "date_acquire", nullable = false)
    private LocalDate dateAcquire;
    
    @ManyToOne
    @JoinColumn(name = "group_id")
    private ItemGroup group;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private ItemModel model;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ItemStatus status;

    @OneToMany(mappedBy = "item")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ItemHistory> historys = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "stock_acquisition_id")
    private StockAcquisition stockAcquisition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStorageLocation() {
        return storageLocation;
    }
    
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public LocalDate getDateAcquire() {
        return dateAcquire;
    }
    
    public void setDateAcquire(LocalDate dateAcquire) {
        this.dateAcquire = dateAcquire;
    }

    public ItemGroup getGroup() {
        return group;
    }

    public void setGroup(ItemGroup itemGroup) {
        this.group = itemGroup;
    }

    public ItemModel getModel() {
        return model;
    }

    public void setModel(ItemModel itemModel) {
        this.model = itemModel;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus ItemStatus) {
        this.status = ItemStatus;
    }

    public Set<ItemHistory> getHistorys() {
        return historys;
    }

    public void setHistorys(Set<ItemHistory> ItemHistorys) {
        this.historys = ItemHistorys;
    }

    public StockAcquisition getStockAcquisition() {
        return stockAcquisition;
    }

    public void setStockAcquisition(StockAcquisition StockAcquisition) {
        this.stockAcquisition = StockAcquisition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        if(item.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + id +
            ", serialNumber='" + serialNumber + "'" +
            ", storageLocation='" + storageLocation + "'" +
            ", dateAcquire='" + dateAcquire + "'" +
            '}';
    }
}
