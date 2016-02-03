package com.faizalsidek.inventory.repository.search;

import com.faizalsidek.inventory.domain.Unit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Unit entity.
 */
public interface UnitSearchRepository extends ElasticsearchRepository<Unit, Long> {
}
