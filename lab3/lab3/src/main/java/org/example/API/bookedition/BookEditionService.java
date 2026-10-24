package org.example.API.bookedition;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookEditionService {
    private final BookEditionRepository repository;

    public BookEditionService(BookEditionRepository repository) { this.repository = repository; }

    public List<BookEditionClass> findAll() { return repository.findAll(); }
    public Optional<BookEditionClass> findById(Long id) { return repository.findById(id); }
    public BookEditionClass create(BookEditionClass edition) { return repository.save(edition); }

    public Optional<BookEditionClass> update(Long id, BookEditionClass updated) {
        return repository.findById(id).map(edition -> {
            if (updated.getTitle() != null) edition.setTitle(updated.getTitle());
            if (updated.getAuthor() != null) edition.setAuthor(updated.getAuthor());
            if (updated.getIsbn() != null) edition.setIsbn(updated.getIsbn());
            return repository.save(edition);
        });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}