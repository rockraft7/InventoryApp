package com.faizalsidek.inventory.service;

import com.faizalsidek.inventory.domain.Author;
import com.faizalsidek.inventory.repository.AuthorRepository;
import com.faizalsidek.inventory.repository.search.AuthorSearchRepository;
import com.faizalsidek.inventory.web.rest.dto.AuthorDTO;
import com.faizalsidek.inventory.web.rest.mapper.AuthorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Author.
 */
@Service
@Transactional
public class AuthorService {

    private final Logger log = LoggerFactory.getLogger(AuthorService.class);
    
    @Inject
    private AuthorRepository authorRepository;
    
    @Inject
    private AuthorMapper authorMapper;
    
    @Inject
    private AuthorSearchRepository authorSearchRepository;
    
    /**
     * Save a author.
     * @return the persisted entity
     */
    public AuthorDTO save(AuthorDTO authorDTO) {
        log.debug("Request to save Author : {}", authorDTO);
        Author author = authorMapper.authorDTOToAuthor(authorDTO);
        author = authorRepository.save(author);
        AuthorDTO result = authorMapper.authorToAuthorDTO(author);
        authorSearchRepository.save(author);
        return result;
    }

    /**
     *  get all the authors.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Author> findAll(Pageable pageable) {
        log.debug("Request to get all Authors");
        Page<Author> result = authorRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one author by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AuthorDTO findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        Author author = authorRepository.findOne(id);
        AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(author);
        return authorDTO;
    }

    /**
     *  delete the  author by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        authorRepository.delete(id);
        authorSearchRepository.delete(id);
    }

    /**
     * search for the author corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<AuthorDTO> search(String query) {
        
        log.debug("REST request to search Authors for query {}", query);
        return StreamSupport
            .stream(authorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(authorMapper::authorToAuthorDTO)
            .collect(Collectors.toList());
    }
}
