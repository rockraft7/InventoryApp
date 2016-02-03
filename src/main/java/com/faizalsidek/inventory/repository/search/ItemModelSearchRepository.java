package com.faizalsidek.inventory.repository.search;

import com.faizalsidek.inventory.domain.ItemModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ItemModel entity.
 */
public interface ItemModelSearchRepository extends ElasticsearchRepository<ItemModel, Long> {
}
