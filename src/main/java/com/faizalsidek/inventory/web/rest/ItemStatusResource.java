package com.faizalsidek.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.faizalsidek.inventory.domain.ItemStatus;
import com.faizalsidek.inventory.repository.ItemStatusRepository;
import com.faizalsidek.inventory.repository.search.ItemStatusSearchRepository;
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
 * REST controller for managing ItemStatus.
 */
@RestController
@RequestMapping("/api")
public class ItemStatusResource {

    private final Logger log = LoggerFactory.getLogger(ItemStatusResource.class);
        
    @Inject
    private ItemStatusRepository itemStatusRepository;
    
    @Inject
    private ItemStatusSearchRepository itemStatusSearchRepository;
    
    /**
     * POST  /itemStatuss -> Create a new itemStatus.
     */
    @RequestMapping(value = "/itemStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemStatus> createItemStatus(@Valid @RequestBody ItemStatus itemStatus) throws URISyntaxException {
        log.debug("REST request to save ItemStatus : {}", itemStatus);
        if (itemStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itemStatus", "idexists", "A new itemStatus cannot already have an ID")).body(null);
        }
        ItemStatus result = itemStatusRepository.save(itemStatus);
        itemStatusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/itemStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itemStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itemStatuss -> Updates an existing itemStatus.
     */
    @RequestMapping(value = "/itemStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemStatus> updateItemStatus(@Valid @RequestBody ItemStatus itemStatus) throws URISyntaxException {
        log.debug("REST request to update ItemStatus : {}", itemStatus);
        if (itemStatus.getId() == null) {
            return createItemStatus(itemStatus);
        }
        ItemStatus result = itemStatusRepository.save(itemStatus);
        itemStatusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itemStatus", itemStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itemStatuss -> get all the itemStatuss.
     */
    @RequestMapping(value = "/itemStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemStatus>> getAllItemStatuss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ItemStatuss");
        Page<ItemStatus> page = itemStatusRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itemStatuss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /itemStatuss/:id -> get the "id" itemStatus.
     */
    @RequestMapping(value = "/itemStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemStatus> getItemStatus(@PathVariable Long id) {
        log.debug("REST request to get ItemStatus : {}", id);
        ItemStatus itemStatus = itemStatusRepository.findOne(id);
        return Optional.ofNullable(itemStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /itemStatuss/:id -> delete the "id" itemStatus.
     */
    @RequestMapping(value = "/itemStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItemStatus(@PathVariable Long id) {
        log.debug("REST request to delete ItemStatus : {}", id);
        itemStatusRepository.delete(id);
        itemStatusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itemStatus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/itemStatuss/:query -> search for the itemStatus corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/itemStatuss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ItemStatus> searchItemStatuss(@PathVariable String query) {
        log.debug("REST request to search ItemStatuss for query {}", query);
        return StreamSupport
            .stream(itemStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
