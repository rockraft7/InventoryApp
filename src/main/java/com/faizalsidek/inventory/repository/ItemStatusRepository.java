package com.faizalsidek.inventory.repository;

import com.faizalsidek.inventory.domain.ItemStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemStatus entity.
 */
public interface ItemStatusRepository extends JpaRepository<ItemStatus,Long> {

}
