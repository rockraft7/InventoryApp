package com.faizalsidek.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.faizalsidek.inventory.domain.ItemModel;
import com.faizalsidek.inventory.repository.ItemModelRepository;
import com.faizalsidek.inventory.repository.search.ItemModelSearchRepository;
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
 * REST controller for managing ItemModel.
 */
@RestController
@RequestMapping("/api")
public class ItemModelResource {

    private final Logger log = LoggerFactory.getLogger(ItemModelResource.class);
        
    @Inject
    private ItemModelRepository itemModelRepository;
    
    @Inject
    private ItemModelSearchRepository itemModelSearchRepository;
    
    /**
     * POST  /itemModels -> Create a new itemModel.
     */
    @RequestMapping(value = "/itemModels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemModel> createItemModel(@Valid @RequestBody ItemModel itemModel) throws URISyntaxException {
        log.debug("REST request to save ItemModel : {}", itemModel);
        if (itemModel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itemModel", "idexists", "A new itemModel cannot already have an ID")).body(null);
        }
        ItemModel result = itemModelRepository.save(itemModel);
        itemModelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/itemModels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itemModel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itemModels -> Updates an existing itemModel.
     */
    @RequestMapping(value = "/itemModels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemModel> updateItemModel(@Valid @RequestBody ItemModel itemModel) throws URISyntaxException {
        log.debug("REST request to update ItemModel : {}", itemModel);
        if (itemModel.getId() == null) {
            return createItemModel(itemModel);
        }
        ItemModel result = itemModelRepository.save(itemModel);
        itemModelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itemModel", itemModel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itemModels -> get all the itemModels.
     */
    @RequestMapping(value = "/itemModels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemModel>> getAllItemModels(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ItemModels");
        Page<ItemModel> page = itemModelRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itemModels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /itemModels/:id -> get the "id" itemModel.
     */
    @RequestMapping(value = "/itemModels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemModel> getItemModel(@PathVariable Long id) {
        log.debug("REST request to get ItemModel : {}", id);
        ItemModel itemModel = itemModelRepository.findOne(id);
        return Optional.ofNullable(itemModel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /itemModels/:id -> delete the "id" itemModel.
     */
    @RequestMapping(value = "/itemModels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItemModel(@PathVariable Long id) {
        log.debug("REST request to delete ItemModel : {}", id);
        itemModelRepository.delete(id);
        itemModelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itemModel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/itemModels/:query -> search for the itemModel corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/itemModels/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ItemModel> searchItemModels(@PathVariable String query) {
        log.debug("REST request to search ItemModels for query {}", query);
        return StreamSupport
            .stream(itemModelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
