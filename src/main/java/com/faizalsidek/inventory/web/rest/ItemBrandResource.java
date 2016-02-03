package com.faizalsidek.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.faizalsidek.inventory.domain.ItemBrand;
import com.faizalsidek.inventory.repository.ItemBrandRepository;
import com.faizalsidek.inventory.repository.search.ItemBrandSearchRepository;
import com.faizalsidek.inventory.web.rest.util.HeaderUtil;
import com.faizalsidek.inventory.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ItemBrand.
 */
@RestController
@RequestMapping("/api")
public class ItemBrandResource {

    private final Logger log = LoggerFactory.getLogger(ItemBrandResource.class);
        
    @Inject
    private ItemBrandRepository itemBrandRepository;
    
    @Inject
    private ItemBrandSearchRepository itemBrandSearchRepository;
    
    /**
     * POST  /itemBrands -> Create a new itemBrand.
     */
    @RequestMapping(value = "/itemBrands",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemBrand> createItemBrand(@Valid @RequestBody ItemBrand itemBrand) throws URISyntaxException {
        log.debug("REST request to save ItemBrand : {}", itemBrand);
        if (itemBrand.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itemBrand", "idexists", "A new itemBrand cannot already have an ID")).body(null);
        }
        ItemBrand result = itemBrandRepository.save(itemBrand);
        itemBrandSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/itemBrands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itemBrand", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itemBrands -> Updates an existing itemBrand.
     */
    @RequestMapping(value = "/itemBrands",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemBrand> updateItemBrand(@Valid @RequestBody ItemBrand itemBrand) throws URISyntaxException {
        log.debug("REST request to update ItemBrand : {}", itemBrand);
        if (itemBrand.getId() == null) {
            return createItemBrand(itemBrand);
        }
        ItemBrand result = itemBrandRepository.save(itemBrand);
        itemBrandSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itemBrand", itemBrand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itemBrands -> get all the itemBrands.
     */
    @RequestMapping(value = "/itemBrands",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemBrand>> getAllItemBrands(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ItemBrands");
        Page<ItemBrand> page = itemBrandRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itemBrands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /itemBrands/:id -> get the "id" itemBrand.
     */
    @RequestMapping(value = "/itemBrands/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemBrand> getItemBrand(@PathVariable Long id) {
        log.debug("REST request to get ItemBrand : {}", id);
        ItemBrand itemBrand = itemBrandRepository.findOne(id);
        return Optional.ofNullable(itemBrand)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /itemBrands/:id -> delete the "id" itemBrand.
     */
    @RequestMapping(value = "/itemBrands/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItemBrand(@PathVariable Long id) {
        log.debug("REST request to delete ItemBrand : {}", id);
        itemBrandRepository.delete(id);
        itemBrandSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itemBrand", id.toString())).build();
    }

    /**
     * SEARCH  /_search/itemBrands/:query -> search for the itemBrand corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/itemBrands/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ItemBrand> searchItemBrands(@PathVariable String query) {
        log.debug("REST request to search ItemBrands for query {}", query);
        return StreamSupport
            .stream(itemBrandSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
