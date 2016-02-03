package com.faizalsidek.inventory.repository.search;

import com.faizalsidek.inventory.domain.ItemGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ItemGroup entity.
 */
public interface ItemGroupSearchRepository extends ElasticsearchRepository<ItemGroup, Long> {
}
