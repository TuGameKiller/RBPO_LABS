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
        return repository.save(user);
    }

    public Optional<UserClass> update(Long id, UserClass updatedUser) {
        return repository.findById(id).map(user -> {
            if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getRole() != null) {
                user.setRole(updatedUser.getRole());
            }
            return repository.save(user);
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