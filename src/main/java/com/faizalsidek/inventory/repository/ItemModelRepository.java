package com.faizalsidek.inventory.repository;

import com.faizalsidek.inventory.domain.ItemModel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemModel entity.
 */
public interface ItemModelRepository extends JpaRepository<ItemModel,Long> {

}
