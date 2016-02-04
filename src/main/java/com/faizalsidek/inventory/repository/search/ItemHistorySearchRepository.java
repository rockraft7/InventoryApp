package com.faizalsidek.inventory.repository.search;

import com.faizalsidek.inventory.domain.ItemHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ItemHistory entity.
 */
public interface ItemHistorySearchRepository extends ElasticsearchRepository<ItemHistory, Long> {
}
