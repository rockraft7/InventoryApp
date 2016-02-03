package com.faizalsidek.inventory.web.rest.mapper;

import com.faizalsidek.inventory.domain.*;
import com.faizalsidek.inventory.web.rest.dto.BookDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Book and its DTO BookDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BookMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.name", target = "authorName")
    BookDTO bookToBookDTO(Book book);

    @Mapping(source = "authorId", target = "author")
    Book bookDTOToBook(BookDTO bookDTO);

    default Author authorFromId(Long id) {
        if (id == null) {
            return null;
        }
        Author author = new Author();
        author.setId(id);
        return author;
    }
}
