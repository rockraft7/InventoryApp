package com.faizalsidek.inventory.repository.search;

import com.faizalsidek.inventory.domain.ItemStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ItemStatus entity.
 */
public interface ItemStatusSearchRepository extends ElasticsearchRepository<ItemStatus, Long> {
}
