package com.faizalsidek.inventory.repository.search;

import com.faizalsidek.inventory.domain.StockAcquisition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the StockAcquisition entity.
 */
public interface StockAcquisitionSearchRepository extends ElasticsearchRepository<StockAcquisition, Long> {
}
