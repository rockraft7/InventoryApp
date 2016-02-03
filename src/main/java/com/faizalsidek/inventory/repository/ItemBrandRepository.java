package com.faizalsidek.inventory.repository;

import com.faizalsidek.inventory.domain.ItemBrand;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemBrand entity.
 */
public interface ItemBrandRepository extends JpaRepository<ItemBrand,Long> {

}
