package com.faizalsidek.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.faizalsidek.inventory.domain.ItemGroup;
import com.faizalsidek.inventory.repository.ItemGroupRepository;
import com.faizalsidek.inventory.repository.search.ItemGroupSearchRepository;
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
 * REST controller for managing ItemGroup.
 */
@RestController
@RequestMapping("/api")
public class ItemGroupResource {

    private final Logger log = LoggerFactory.getLogger(ItemGroupResource.class);
        
    @Inject
    private ItemGroupRepository itemGroupRepository;
    
    @Inject
    private ItemGroupSearchRepository itemGroupSearchRepository;
    
    /**
     * POST  /itemGroups -> Create a new itemGroup.
     */
    @RequestMapping(value = "/itemGroups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemGroup> createItemGroup(@Valid @RequestBody ItemGroup itemGroup) throws URISyntaxException {
        log.debug("REST request to save ItemGroup : {}", itemGroup);
        if (itemGroup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itemGroup", "idexists", "A new itemGroup cannot already have an ID")).body(null);
        }
        ItemGroup result = itemGroupRepository.save(itemGroup);
        itemGroupSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/itemGroups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itemGroup", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itemGroups -> Updates an existing itemGroup.
     */
    @RequestMapping(value = "/itemGroups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemGroup> updateItemGroup(@Valid @RequestBody ItemGroup itemGroup) throws URISyntaxException {
        log.debug("REST request to update ItemGroup : {}", itemGroup);
        if (itemGroup.getId() == null) {
            return createItemGroup(itemGroup);
        }
        ItemGroup result = itemGroupRepository.save(itemGroup);
        itemGroupSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itemGroup", itemGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itemGroups -> get all the itemGroups.
     */
    @RequestMapping(value = "/itemGroups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemGroup>> getAllItemGroups(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ItemGroups");
        Page<ItemGroup> page = itemGroupRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itemGroups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /itemGroups/:id -> get the "id" itemGroup.
     */
    @RequestMapping(value = "/itemGroups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemGroup> getItemGroup(@PathVariable Long id) {
        log.debug("REST request to get ItemGroup : {}", id);
        ItemGroup itemGroup = itemGroupRepository.findOne(id);
        return Optional.ofNullable(itemGroup)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /itemGroups/:id -> delete the "id" itemGroup.
     */
    @RequestMapping(value = "/itemGroups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItemGroup(@PathVariable Long id) {
        log.debug("REST request to delete ItemGroup : {}", id);
        itemGroupRepository.delete(id);
        itemGroupSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itemGroup", id.toString())).build();
    }

    /**
     * SEARCH  /_search/itemGroups/:query -> search for the itemGroup corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/itemGroups/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ItemGroup> searchItemGroups(@PathVariable String query) {
        log.debug("REST request to search ItemGroups for query {}", query);
        return StreamSupport
            .stream(itemGroupSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
