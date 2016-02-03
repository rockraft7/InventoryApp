package com.faizalsidek.inventory.repository;

import com.faizalsidek.inventory.domain.Item;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Item entity.
 */
public interface ItemRepository extends JpaRepository<Item,Long> {

}
