package org.example.API.user;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final List<UserClass> storage = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<UserClass> findAll() {
        return storage;
    }

    public Optional<UserClass> findById(Long id) {
        return storage.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public UserClass save(UserClass user) {
        if (user.getId() == null) {
            user.setId(idCounter.getAndIncrement());
            storage.add(user);
        } else {
            delete(user.getId());
            storage.add(user);
        }
        return user;
    }

    public boolean delete(Long id) {
        return storage.removeIf(u -> u.getId().equals(id));
    }
}