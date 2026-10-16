package org.example.API.copy;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CopyRepository {
    private final List<CopyClass> storage = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<CopyClass> findAll() {
        return storage;
    }

    public Optional<CopyClass> findById(Long id) {
        return storage.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public CopyClass save(CopyClass copy) {
        if (copy.getId() == null) {
            copy.setId(idCounter.getAndIncrement());
            storage.add(copy);
        } else {
            delete(copy.getId());
            storage.add(copy);
        }
        return copy;
    }

    public boolean delete(Long id) {
        return storage.removeIf(c -> c.getId().equals(id));
    }
}