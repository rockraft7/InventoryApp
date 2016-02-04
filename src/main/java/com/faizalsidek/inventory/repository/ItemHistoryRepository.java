package com.faizalsidek.inventory.repository;

import com.faizalsidek.inventory.domain.ItemHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemHistory entity.
 */
public interface ItemHistoryRepository extends JpaRepository<ItemHistory,Long> {

}
