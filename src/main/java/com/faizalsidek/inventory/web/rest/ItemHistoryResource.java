package com.faizalsidek.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.faizalsidek.inventory.domain.ItemHistory;
import com.faizalsidek.inventory.repository.ItemHistoryRepository;
import com.faizalsidek.inventory.repository.search.ItemHistorySearchRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ItemHistory.
 */
@RestController
@RequestMapping("/api")
public class ItemHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ItemHistoryResource.class);
        
    @Inject
    private ItemHistoryRepository itemHistoryRepository;
    
    @Inject
    private ItemHistorySearchRepository itemHistorySearchRepository;
    
    /**
     * POST  /itemHistorys -> Create a new itemHistory.
     */
    @RequestMapping(value = "/itemHistorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemHistory> createItemHistory(@RequestBody ItemHistory itemHistory) throws URISyntaxException {
        log.debug("REST request to save ItemHistory : {}", itemHistory);
        if (itemHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itemHistory", "idexists", "A new itemHistory cannot already have an ID")).body(null);
        }
        ItemHistory result = itemHistoryRepository.save(itemHistory);
        itemHistorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/itemHistorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itemHistory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itemHistorys -> Updates an existing itemHistory.
     */
    @RequestMapping(value = "/itemHistorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemHistory> updateItemHistory(@RequestBody ItemHistory itemHistory) throws URISyntaxException {
        log.debug("REST request to update ItemHistory : {}", itemHistory);
        if (itemHistory.getId() == null) {
            return createItemHistory(itemHistory);
        }
        ItemHistory result = itemHistoryRepository.save(itemHistory);
        itemHistorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itemHistory", itemHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itemHistorys -> get all the itemHistorys.
     */
    @RequestMapping(value = "/itemHistorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemHistory>> getAllItemHistorys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ItemHistorys");
        Page<ItemHistory> page = itemHistoryRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itemHistorys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /itemHistorys/:id -> get the "id" itemHistory.
     */
    @RequestMapping(value = "/itemHistorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemHistory> getItemHistory(@PathVariable Long id) {
        log.debug("REST request to get ItemHistory : {}", id);
        ItemHistory itemHistory = itemHistoryRepository.findOne(id);
        return Optional.ofNullable(itemHistory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /itemHistorys/:id -> delete the "id" itemHistory.
     */
    @RequestMapping(value = "/itemHistorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItemHistory(@PathVariable Long id) {
        log.debug("REST request to delete ItemHistory : {}", id);
        itemHistoryRepository.delete(id);
        itemHistorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itemHistory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/itemHistorys/:query -> search for the itemHistory corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/itemHistorys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ItemHistory> searchItemHistorys(@PathVariable String query) {
        log.debug("REST request to search ItemHistorys for query {}", query);
        return StreamSupport
            .stream(itemHistorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
