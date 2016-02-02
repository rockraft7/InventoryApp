package com.faizalsidek.inventory.service;

import com.faizalsidek.inventory.domain.Author;
import com.faizalsidek.inventory.web.rest.dto.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Author.
 */
public interface AuthorService {

    /**
     * Save a author.
     * @return the persisted entity
     */
    public AuthorDTO save(AuthorDTO authorDTO);

    /**
     *  get all the authors.
     *  @return the list of entities
     */
    public Page<Author> findAll(Pageable pageable);

    /**
     *  get the "id" author.
     *  @return the entity
     */
    public AuthorDTO findOne(Long id);

    /**
     *  delete the "id" author.
     */
    public void delete(Long id);

    /**
     * search for the author corresponding
     * to the query.
     */
    public List<AuthorDTO> search(String query);
}
