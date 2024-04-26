package com.polarbookshop.catalogservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Iterable<Book> viewBookList() {
        return bookRepository.findAll();
    }

    public Book viewBookDetails(String isbn) {
        return bookRepository.findByIsbn(isbn)
                             .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBookToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.isbn())) {
            throw new BookAlreadyExistsException(book.isbn());
        }
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                             .map(existingBook -> {
                                 var bookToUpdate = new Book(
                                         existingBook.id(),
                                         existingBook.isbn(),
                                         book.title(),
                                         book.author(),
                                         book.price(),
                                         book.publisher(),
                                         existingBook.createdDate(),
                                         existingBook.createdBy(),
                                         existingBook.lastModifiedDate(),
                                         existingBook.lastModifiedBy(),
                                         existingBook.version()); // 버전은 업데이트가 성공하면 자동으로 증가
                                 return bookRepository.save(bookToUpdate);
                             })
                             .orElseGet(() -> addBookToCatalog(book));
    }
}
