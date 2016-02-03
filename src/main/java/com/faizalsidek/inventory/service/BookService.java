package com.faizalsidek.inventory.service;

import com.faizalsidek.inventory.domain.Book;
import com.faizalsidek.inventory.repository.BookRepository;
import com.faizalsidek.inventory.repository.search.BookSearchRepository;
import com.faizalsidek.inventory.web.rest.dto.BookDTO;
import com.faizalsidek.inventory.web.rest.mapper.BookMapper;
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
 * Service Implementation for managing Book.
 */
@Service
@Transactional
public class BookService {

    private final Logger log = LoggerFactory.getLogger(BookService.class);
    
    @Inject
    private BookRepository bookRepository;
    
    @Inject
    private BookMapper bookMapper;
    
    @Inject
    private BookSearchRepository bookSearchRepository;
    
    /**
     * Save a book.
     * @return the persisted entity
     */
    public BookDTO save(BookDTO bookDTO) {
        log.debug("Request to save Book : {}", bookDTO);
        Book book = bookMapper.bookDTOToBook(bookDTO);
        book = bookRepository.save(book);
        BookDTO result = bookMapper.bookToBookDTO(book);
        bookSearchRepository.save(book);
        return result;
    }

    /**
     *  get all the books.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        Page<Book> result = bookRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one book by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BookDTO findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        Book book = bookRepository.findOne(id);
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);
        return bookDTO;
    }

    /**
     *  delete the  book by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.delete(id);
        bookSearchRepository.delete(id);
    }

    /**
     * search for the book corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<BookDTO> search(String query) {
        
        log.debug("REST request to search Books for query {}", query);
        return StreamSupport
            .stream(bookSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(bookMapper::bookToBookDTO)
            .collect(Collectors.toList());
    }
}
