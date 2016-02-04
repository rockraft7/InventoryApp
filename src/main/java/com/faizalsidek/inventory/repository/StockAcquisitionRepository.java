package com.faizalsidek.inventory.repository;

import com.faizalsidek.inventory.domain.StockAcquisition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockAcquisition entity.
 */
public interface StockAcquisitionRepository extends JpaRepository<StockAcquisition,Long> {

}
