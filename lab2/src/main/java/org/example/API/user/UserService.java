package org.example.API.user;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserClass> findAll() {
        return repository.findAll();
    }

    public Optional<UserClass> findById(Long id) {
        return repository.findById(id);
    }

    public UserClass create(UserClass user) {
        // Установка роли по умолчанию, если она не передана
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("READER");
        }
        return repository.save(user);
    }

    public Optional<UserClass> update(Long id, UserClass updatedUser) {
        return repository.findById(id).map(user -> {
            if (updatedUser.getName() != null) user.setName(updatedUser.getName());
            if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
            if (updatedUser.getRole() != null) user.setRole(updatedUser.getRole());
            return repository.save(user);
        });
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}