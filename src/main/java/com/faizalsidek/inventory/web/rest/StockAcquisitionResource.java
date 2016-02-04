package com.faizalsidek.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.faizalsidek.inventory.domain.StockAcquisition;
import com.faizalsidek.inventory.repository.StockAcquisitionRepository;
import com.faizalsidek.inventory.repository.search.StockAcquisitionSearchRepository;
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
 * REST controller for managing StockAcquisition.
 */
@RestController
@RequestMapping("/api")
public class StockAcquisitionResource {

    private final Logger log = LoggerFactory.getLogger(StockAcquisitionResource.class);
        
    @Inject
    private StockAcquisitionRepository stockAcquisitionRepository;
    
    @Inject
    private StockAcquisitionSearchRepository stockAcquisitionSearchRepository;
    
    /**
     * POST  /stockAcquisitions -> Create a new stockAcquisition.
     */
    @RequestMapping(value = "/stockAcquisitions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockAcquisition> createStockAcquisition(@Valid @RequestBody StockAcquisition stockAcquisition) throws URISyntaxException {
        log.debug("REST request to save StockAcquisition : {}", stockAcquisition);
        if (stockAcquisition.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stockAcquisition", "idexists", "A new stockAcquisition cannot already have an ID")).body(null);
        }
        StockAcquisition result = stockAcquisitionRepository.save(stockAcquisition);
        stockAcquisitionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/stockAcquisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stockAcquisition", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stockAcquisitions -> Updates an existing stockAcquisition.
     */
    @RequestMapping(value = "/stockAcquisitions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockAcquisition> updateStockAcquisition(@Valid @RequestBody StockAcquisition stockAcquisition) throws URISyntaxException {
        log.debug("REST request to update StockAcquisition : {}", stockAcquisition);
        if (stockAcquisition.getId() == null) {
            return createStockAcquisition(stockAcquisition);
        }
        StockAcquisition result = stockAcquisitionRepository.save(stockAcquisition);
        stockAcquisitionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stockAcquisition", stockAcquisition.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stockAcquisitions -> get all the stockAcquisitions.
     */
    @RequestMapping(value = "/stockAcquisitions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StockAcquisition>> getAllStockAcquisitions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockAcquisitions");
        Page<StockAcquisition> page = stockAcquisitionRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stockAcquisitions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stockAcquisitions/:id -> get the "id" stockAcquisition.
     */
    @RequestMapping(value = "/stockAcquisitions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockAcquisition> getStockAcquisition(@PathVariable Long id) {
        log.debug("REST request to get StockAcquisition : {}", id);
        StockAcquisition stockAcquisition = stockAcquisitionRepository.findOne(id);
        return Optional.ofNullable(stockAcquisition)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockAcquisitions/:id -> delete the "id" stockAcquisition.
     */
    @RequestMapping(value = "/stockAcquisitions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockAcquisition(@PathVariable Long id) {
        log.debug("REST request to delete StockAcquisition : {}", id);
        stockAcquisitionRepository.delete(id);
        stockAcquisitionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockAcquisition", id.toString())).build();
    }

    /**
     * SEARCH  /_search/stockAcquisitions/:query -> search for the stockAcquisition corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/stockAcquisitions/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StockAcquisition> searchStockAcquisitions(@PathVariable String query) {
        log.debug("REST request to search StockAcquisitions for query {}", query);
        return StreamSupport
            .stream(stockAcquisitionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
