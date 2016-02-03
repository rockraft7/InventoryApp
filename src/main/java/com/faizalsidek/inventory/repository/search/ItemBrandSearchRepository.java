package com.faizalsidek.inventory.repository.search;

import com.faizalsidek.inventory.domain.ItemBrand;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ItemBrand entity.
 */
public interface ItemBrandSearchRepository extends ElasticsearchRepository<ItemBrand, Long> {
}
