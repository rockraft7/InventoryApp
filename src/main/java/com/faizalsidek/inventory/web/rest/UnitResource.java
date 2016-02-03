package com.faizalsidek.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.faizalsidek.inventory.domain.Unit;
import com.faizalsidek.inventory.repository.UnitRepository;
import com.faizalsidek.inventory.repository.search.UnitSearchRepository;
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
 * REST controller for managing Unit.
 */
@RestController
@RequestMapping("/api")
public class UnitResource {

    private final Logger log = LoggerFactory.getLogger(UnitResource.class);
        
    @Inject
    private UnitRepository unitRepository;
    
    @Inject
    private UnitSearchRepository unitSearchRepository;
    
    /**
     * POST  /units -> Create a new unit.
     */
    @RequestMapping(value = "/units",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unit> createUnit(@Valid @RequestBody Unit unit) throws URISyntaxException {
        log.debug("REST request to save Unit : {}", unit);
        if (unit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("unit", "idexists", "A new unit cannot already have an ID")).body(null);
        }
        Unit result = unitRepository.save(unit);
        unitSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("unit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /units -> Updates an existing unit.
     */
    @RequestMapping(value = "/units",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unit> updateUnit(@Valid @RequestBody Unit unit) throws URISyntaxException {
        log.debug("REST request to update Unit : {}", unit);
        if (unit.getId() == null) {
            return createUnit(unit);
        }
        Unit result = unitRepository.save(unit);
        unitSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("unit", unit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /units -> get all the units.
     */
    @RequestMapping(value = "/units",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Unit>> getAllUnits(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Units");
        Page<Unit> page = unitRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /units/:id -> get the "id" unit.
     */
    @RequestMapping(value = "/units/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unit> getUnit(@PathVariable Long id) {
        log.debug("REST request to get Unit : {}", id);
        Unit unit = unitRepository.findOne(id);
        return Optional.ofNullable(unit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /units/:id -> delete the "id" unit.
     */
    @RequestMapping(value = "/units/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        log.debug("REST request to delete Unit : {}", id);
        unitRepository.delete(id);
        unitSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("unit", id.toString())).build();
    }

    /**
     * SEARCH  /_search/units/:query -> search for the unit corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/units/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Unit> searchUnits(@PathVariable String query) {
        log.debug("REST request to search Units for query {}", query);
        return StreamSupport
            .stream(unitSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
