package com.faizalsidek.inventory.repository;

import com.faizalsidek.inventory.domain.Unit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Unit entity.
 */
public interface UnitRepository extends JpaRepository<Unit,Long> {

}
