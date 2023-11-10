package com.example.egor.Controllers;
import com.example.egor.Entities.User;
import com.example.egor.Repositories.BookRepository;
import com.example.egor.Entities.Products.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository bookRepository;
    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<String> createBook(@RequestHeader("Authorization") String authorizationHeader,
                                           @RequestBody Book book) {
        User authenticatedUser = authenticationController.getUserByToken(authorizationHeader);
        if (authenticatedUser == null) {
            return new ResponseEntity<>("Некорректный JWT токен",HttpStatus.FORBIDDEN);
        }
        else if (Objects.equals(authenticatedUser.getRole(), "SELLER") || Objects.equals(authenticatedUser.getRole(), "ADMIN")) {
            book.setQuantity(book.getQuantity());
            book.setSeller(authenticatedUser);
            Book savedBook = bookRepository.save(book);
            return new ResponseEntity<>(savedBook.toString(), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>("Ваша роль не SELLER", HttpStatus.FORBIDDEN);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Long id, @RequestBody Book updatedBook) {
        User authenticatedUser = authenticationController.getUserByToken(authorizationHeader);
        Book existingBook;
        if (!bookRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            existingBook = bookRepository.getById(id);
        }
        if (authenticatedUser == null) {
            return new ResponseEntity<>("Некорректный JWT токен",
                    HttpStatus.FORBIDDEN);
        }
        else if (existingBook.getSeller().getId() != authenticatedUser.getId() || !Objects.equals(authenticatedUser.getRole(), "ADMIN")) {
            return new ResponseEntity<>("Вы не являетесь продавцом этой книги",
                    HttpStatus.FORBIDDEN);
        }
        else if (Objects.equals(authenticatedUser.getRole(), "SELLER") || Objects.equals(authenticatedUser.getRole(), "ADMIN")) {
            existingBook.setName(updatedBook.getName());
            existingBook.setQuantity(updatedBook.getQuantity());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setProductType(updatedBook.getProductType());
            existingBook.setPrice(updatedBook.getPrice());
            bookRepository.save(existingBook);
            return new ResponseEntity<>(existingBook.toString(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Ваша роль не SELLER", HttpStatus.FORBIDDEN);
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Long id) {
        User authenticatedUser = authenticationController.getUserByToken(authorizationHeader);
        if ((authenticatedUser == null || bookRepository.findById(id).get().getSeller() != authenticatedUser) && !Objects.equals(Objects.requireNonNull(authenticatedUser).getRole(), "ADMIN")) {
            return new ResponseEntity<>("Некорректный JWT токен или вы не являетесь продавцом этой книги",
                    HttpStatus.FORBIDDEN);
        }
        else if (Objects.equals(authenticatedUser.getRole(), "SELLER") || (Objects.equals(authenticatedUser.getRole(), "ADMIN"))) {
            if (bookRepository.existsById(id)) {
                bookRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        else {
            return new ResponseEntity<>("Ваша роль не SELLER", HttpStatus.FORBIDDEN);
        }
    }
}
