package com.faizalsidek.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.faizalsidek.inventory.domain.Item;
import com.faizalsidek.inventory.repository.ItemRepository;
import com.faizalsidek.inventory.repository.search.ItemSearchRepository;
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
 * REST controller for managing Item.
 */
@RestController
@RequestMapping("/api")
public class ItemResource {

    private final Logger log = LoggerFactory.getLogger(ItemResource.class);
        
    @Inject
    private ItemRepository itemRepository;
    
    @Inject
    private ItemSearchRepository itemSearchRepository;
    
    /**
     * POST  /items -> Create a new item.
     */
    @RequestMapping(value = "/items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Item> createItem(@RequestBody Item item) throws URISyntaxException {
        log.debug("REST request to save Item : {}", item);
        if (item.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("item", "idexists", "A new item cannot already have an ID")).body(null);
        }
        Item result = itemRepository.save(item);
        itemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("item", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /items -> Updates an existing item.
     */
    @RequestMapping(value = "/items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Item> updateItem(@RequestBody Item item) throws URISyntaxException {
        log.debug("REST request to update Item : {}", item);
        if (item.getId() == null) {
            return createItem(item);
        }
        Item result = itemRepository.save(item);
        itemSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("item", item.getId().toString()))
            .body(result);
    }

    /**
     * GET  /items -> get all the items.
     */
    @RequestMapping(value = "/items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Item>> getAllItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Items");
        Page<Item> page = itemRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /items/:id -> get the "id" item.
     */
    @RequestMapping(value = "/items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        log.debug("REST request to get Item : {}", id);
        Item item = itemRepository.findOne(id);
        return Optional.ofNullable(item)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /items/:id -> delete the "id" item.
     */
    @RequestMapping(value = "/items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.debug("REST request to delete Item : {}", id);
        itemRepository.delete(id);
        itemSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("item", id.toString())).build();
    }

    /**
     * SEARCH  /_search/items/:query -> search for the item corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/items/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Item> searchItems(@PathVariable String query) {
        log.debug("REST request to search Items for query {}", query);
        return StreamSupport
            .stream(itemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
