package org.example.API.bookedition;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookEditionService {
    private final BookEditionRepository repository;

    public BookEditionService(BookEditionRepository repository) {
        this.repository = repository;
    }

    public List<BookEditionClass> findAll() {
        return repository.findAll();
    }

    public Optional<BookEditionClass> findById(Long id) {
        return repository.findById(id);
    }

    public BookEditionClass create(BookEditionClass book) {
        return repository.save(book);
    }

    public Optional<BookEditionClass> update(Long id, BookEditionClass updatedBook) {
        return repository.findById(id).map(book -> {
            if (updatedBook.getTitle() != null) book.setTitle(updatedBook.getTitle());
            if (updatedBook.getAuthor() != null) book.setAuthor(updatedBook.getAuthor());
            if (updatedBook.getIsbn() != null) book.setIsbn(updatedBook.getIsbn());
            return repository.save(book);
        });
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}