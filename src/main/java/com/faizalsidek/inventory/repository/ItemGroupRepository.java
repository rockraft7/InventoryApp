package com.faizalsidek.inventory.repository;

import com.faizalsidek.inventory.domain.ItemGroup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemGroup entity.
 */
public interface ItemGroupRepository extends JpaRepository<ItemGroup,Long> {

}
