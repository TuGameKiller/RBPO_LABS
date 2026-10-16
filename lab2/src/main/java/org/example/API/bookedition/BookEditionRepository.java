package org.example.API.bookedition;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BookEditionRepository {
    private final List<BookEditionClass> storage = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<BookEditionClass> findAll() {
        return storage;
    }

    public Optional<BookEditionClass> findById(Long id) {
        return storage.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public BookEditionClass save(BookEditionClass book) {
        if (book.getId() == null) {
            book.setId(idCounter.getAndIncrement());
            storage.add(book);
        } else {
            delete(book.getId());
            storage.add(book);
        }
        return book;
    }

    public boolean delete(Long id) {
        return storage.removeIf(b -> b.getId().equals(id));
    }
}