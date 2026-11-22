package org.example.API.bookedition;

import org.example.API.EntityHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public BookEditionClass create(BookEditionClass edition) {
        EntityHelper.requireNonBlank(edition.getTitle(), "Не указано название издания (title)");
        EntityHelper.requireNonBlank(edition.getAuthor(), "Не указан автор издания (author)");
        String isbn = EntityHelper.requireNonBlank(edition.getIsbn(), "Не указан ISBN издания");

        if (repository.existsByIsbn(isbn)) {
            throw new IllegalArgumentException("Издание с ISBN '" + isbn + "' уже существует");
        }
        return repository.save(edition);
    }

    @Transactional
    public Optional<BookEditionClass> update(Long id, BookEditionClass updated) {
        return repository.findById(id).map(edition -> {
            EntityHelper.updateIfPresent(updated.getTitle(), "Название издания не может быть пустым", edition::setTitle);
            EntityHelper.updateIfPresent(updated.getAuthor(), "Автор издания не может быть пустым", edition::setAuthor);
            EntityHelper.updateIfPresent(updated.getIsbn(), "ISBN не может быть пустым", isbn -> {
                if (repository.existsByIsbnAndIdNot(isbn, id)) {
                    throw new IllegalArgumentException("Издание с ISBN '" + isbn + "' уже существует");
                }
                edition.setIsbn(isbn);
            });
            return repository.save(edition);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}